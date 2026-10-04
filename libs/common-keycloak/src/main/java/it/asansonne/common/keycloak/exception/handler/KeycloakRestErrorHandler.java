package it.asansonne.common.keycloak.exception.handler;

import static it.asansonne.common.keycloak.enums.ErrorMessage.KEYCLOAK_CALL_ERROR;

import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.rest.exception.handler.RestErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component("keycloakRestErrorHandler")
public class KeycloakRestErrorHandler implements RestErrorHandler {

  @Override
  public RuntimeException handle(String url, RestClientException exception) {
    if (exception instanceof HttpStatusCodeException ex) {
      HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
      return new KeycloakCallException(
          KEYCLOAK_CALL_ERROR.getCode(),
          url,
          status,
          ex.getResponseBodyAsString()
      );
    }
    return new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), url, exception.getMessage());
  }
}
