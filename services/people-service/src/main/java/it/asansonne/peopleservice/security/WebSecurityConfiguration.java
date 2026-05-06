package it.asansonne.peopleservice.security;

import it.asansonne.common.core.handler.AuthorizationAuthenticationHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * The type Web security configuration.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {
  private final AuthorizationAuthenticationHandler handler;
  /**
   * The Graphql path.
   */
  @Value("${spring.graphql.http.path}")
  String graphqlPath;

  /**
   * Filter chain security filter chain.
   *
   * @param http                    the http
   * @param authenticationConverter the authentication converter
   * @return the security filter chain
   */
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http,
                                            KeycloakAuthenticationConverter authenticationConverter) {
    log.debug("Graphql path: {}", graphqlPath);
    return http
        .cors(Customizer.withDefaults())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter)))
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(handler)
            .accessDeniedHandler(handler))
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/graphiql", "/graphiql/**").permitAll()
            .requestMatchers(graphqlPath).permitAll()
            .anyRequest().denyAll())
        .build();
  }

  /**
   * The type Keycloak authentication converter.
   */
  @Component
  @RequiredArgsConstructor
  protected static class KeycloakAuthenticationConverter
      implements Converter<Jwt, JwtAuthenticationToken> {
    private final KeycloakAuthoritiesConverter authoritiesConverter;

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
      log.debug("Converting JWT {} to AuthenticationToken", jwt.getTokenValue());
      return new JwtAuthenticationToken(
          jwt, authoritiesConverter.convert(jwt), jwt.getSubject()
      );
    }

    /**
     * The type Keycloak authorities converter.
     */
    @Component
    static class KeycloakAuthoritiesConverter
        implements Converter<Jwt, List<SimpleGrantedAuthority>> {

      @Override
      @SuppressWarnings({"unchecked"})
      public List<SimpleGrantedAuthority> convert(@NonNull Jwt jwt) {
        List<String> roles = (List<String>) jwt.getClaims()
            .getOrDefault("roles", List.of());

        return roles.stream()
            .map(role -> "ROLE_" + role)
            .map(SimpleGrantedAuthority::new)
            .toList();
      }
    }
  }

}
