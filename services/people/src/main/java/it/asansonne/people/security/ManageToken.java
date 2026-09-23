package it.asansonne.people.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Service
@AllArgsConstructor
public class ManageToken extends OncePerRequestFilter {
  private final OAuth2AuthorizedClientService authorizedClientService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  )
      throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken oauthToken) {
      OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
          oauthToken.getAuthorizedClientRegistrationId(),
          oauthToken.getName()
      );

      if (client != null) {
        Instant expiresAt = client.getAccessToken().getExpiresAt();
        if (expiresAt != null && expiresAt.isBefore(Instant.now().plusSeconds(60))) {
          authorizedClientService.saveAuthorizedClient(refreshToken(client), oauthToken);
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private OAuth2AuthorizedClient refreshToken(OAuth2AuthorizedClient client) {
    assert client.getRefreshToken() != null;
    OAuth2AccessTokenResponse response =
        new RestClientRefreshTokenTokenResponseClient()
            .getTokenResponse(
                new OAuth2RefreshTokenGrantRequest(
                    client.getClientRegistration(),
                    client.getAccessToken(),
                    client.getRefreshToken()
                )
            );

    OAuth2AuthorizedClient updatedClient = new OAuth2AuthorizedClient(
        client.getClientRegistration(),
        client.getPrincipalName(),
        response.getAccessToken(),
        response.getRefreshToken()
    );
    authorizedClientService.saveAuthorizedClient(updatedClient, null);
    return updatedClient;
  }
}
