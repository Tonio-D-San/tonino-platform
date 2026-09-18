package it.asansonne.common.keycloak.utils;

import static it.asansonne.common.core.enums.ErrorMessage.JWT_ERROR;

import it.asansonne.common.core.exception.custom.DataIntegrityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * The type Jwt rest headers provider.
 */
@Component
public class JwtRestHeadersProvider implements RestHeadersProvider {
  @Override
  public HttpHeaders build() {

    if (!(SecurityContextHolder.getContext()
        .getAuthentication() instanceof JwtAuthenticationToken jwt)) {
      throw new DataIntegrityException(JWT_ERROR.getCode());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwt.getToken().getTokenValue());
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
