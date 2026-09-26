package it.asansonne.people.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.asansonne.common.core.handler.AuthorizationAuthenticationHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final AuthorizationAuthenticationHandler handler;
  private final ManageToken manageToken;

  @Value("${api.base-path}")
  String apiBasePath;
  @Value("${keycloak.host.realm:err-error}")
  String localhostIssuer;
  @Value("${application.issuer.ngrok:ngrok-error}")
  String ngrokIssuer;

  @Bean
  protected SecurityFilterChain filterChain(
      HttpSecurity http, KeycloakAuthenticationConverter authenticationConverter
  ) {
    log.info("Configuring security filter chain");
    return http
        .addFilterBefore(manageToken, UsernamePasswordAuthenticationFilter.class)
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(oauth2 -> oauth2
            .authenticationManagerResolver(
                multiIssuerAuthManagerResolver(authenticationConverter)
            )
        ).authorizeHttpRequests(requests -> requests
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/openapi-docs",
                "/openapi-docs/**",
                "/v3/api-docs/**",
                "/actuator/health",
                "/actuator/info",
                "/error"
            ).permitAll()
            .requestMatchers(apiBasePath + "/**").authenticated()
            .anyRequest().permitAll()
        ).sessionManagement(session ->
            {
              session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
              session.maximumSessions(1).maxSessionsPreventsLogin(false);
            }
        ).exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(handler)
            .accessDeniedHandler(handler)
        ).build();
  }

  @Component
  @RequiredArgsConstructor
  protected static class KeycloakAuthenticationConverter
      implements Converter<Jwt, JwtAuthenticationToken> {
    private final KeycloakAuthoritiesConverter authoritiesConverter;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
      return new JwtAuthenticationToken(
          jwt, Objects.requireNonNull(authoritiesConverter.convert(jwt)), Objects.requireNonNull(jwt.getSubject())
      );
    }

    @Component
    static class KeycloakAuthoritiesConverter
        implements Converter<Jwt, List<SimpleGrantedAuthority>> {
      @Value("${keycloak.client.id:${keycloak.client-id}}")
      private String clientId;

      @Override
      @SuppressWarnings({"unchecked"})
      public List<SimpleGrantedAuthority> convert(@NonNull Jwt jwt) {
        final var realmAccess = (Map<String, Object>) jwt.getClaims()
            .getOrDefault("resource_access", Map.of());
        final var client = (Map<String, Object>) realmAccess.getOrDefault(clientId, Map.of());
        final var roles = (List<String>) client
            .getOrDefault("roles", List.of());
        final List<String> prefixRoles = roles.stream().map(s -> "ROLE_" + s).toList();
        final String clientScope = (String) jwt.getClaims()
            .getOrDefault("scope", "");
        final List<String> prefixScope = Arrays.stream(clientScope.split(" "))
            .map(s -> "SCOPE_" + s).toList();
        List<String> authorities = new ArrayList<>();
        authorities.addAll(prefixRoles);
        authorities.addAll(prefixScope);
        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
      }
    }
  }

  @Bean
  AuthenticationManagerResolver<HttpServletRequest> multiIssuerAuthManagerResolver(
      KeycloakAuthenticationConverter authenticationConverter
  ) {
    Map<String, AuthenticationManager> managers = new ConcurrentHashMap<>();
    var trustedIssuers = Set.of(
        localhostIssuer, ngrokIssuer
    );

    return request -> authentication -> {
      log.info("Request: {}", request);
      String token = extractBearer(request);
      if (token == null) {
        throw new BadCredentialsException("Missing Bearer token");
      }

      String issuer = extractIssuerUnverified(token);
      if (issuer == null) {
        throw new BadCredentialsException("Missing iss claim");
      }

      if (!trustedIssuers.contains(issuer)) {
        throw new BadCredentialsException("Untrusted issuer: " + issuer);
      }

      return managers.computeIfAbsent(
          issuer,
          iss -> {
            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(
                JwtDecoders.fromIssuerLocation(iss)
            );
            provider.setJwtAuthenticationConverter(authenticationConverter);
            return provider::authenticate;
          }
      ).authenticate(authentication);
    };
  }

  private static String extractBearer(HttpServletRequest request) {
    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (auth == null || !auth.startsWith("Bearer ")) {
      return null;
    }
    return auth.substring("Bearer ".length()).trim();
  }

  private static final ObjectMapper OM = new ObjectMapper();

  private static String extractIssuerUnverified(String jwt) {
    try {
      if (jwt == null) {
        return null;
      }
      String[] parts = jwt.split("\\.");
      if (parts.length != 3) {
        return null;
      }
      String payloadB64 = parts[1];
      if (payloadB64.length() > 4096) {
        return null;
      }
      if (!payloadB64.matches("^[A-Za-z0-9_\\-]+$")) {
        return null;
      }
      byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadB64);
      if (payloadBytes.length > 4096) {
        return null;
      }
      Object iss = OM.readValue(
          payloadBytes,
          Map.class
      ).get("iss");
      if (iss == null) {
        return null;
      }
      String issStr = iss.toString();
      return issStr.length() <= 512 ? issStr : null;
    } catch (Exception _) {
      return null;
    }
  }
}
