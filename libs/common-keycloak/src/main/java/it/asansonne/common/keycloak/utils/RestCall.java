package it.asansonne.common.keycloak.utils;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;
import static it.asansonne.common.core.enums.ErrorMessage.FORBIDDEN;
import static it.asansonne.common.core.enums.ErrorMessage.UNAUTHORIZED_ACCESS;
import static it.asansonne.common.core.enums.ErrorMessage.UNCAUGHT_ERROR;
import static it.asansonne.common.keycloak.enums.ErrorMessage.CONFLICT_ERROR;
import static it.asansonne.common.keycloak.enums.ErrorMessage.KEYCLOAK_CALL_ERROR;
import static it.asansonne.common.keycloak.enums.ErrorMessage.NULL_HTTP_STATUS_CODE;
import static it.asansonne.common.keycloak.enums.ErrorMessage.URL_NOT_FOUND;

import it.asansonne.common.core.exception.custom.BadRequestException;
import it.asansonne.common.core.exception.custom.ForbiddenException;
import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.NullStatusException;
import it.asansonne.common.core.exception.custom.UnauthorizedException;
import it.asansonne.common.core.exception.custom.UncaughtException;
import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.exception.ConflictException;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RestCall {

  private final RestTemplate restTemplate;

  public static final String EMAIL = "email";
  public static final String ENABLED = "enabled";

  public <T> ResponseEntity<T> doRequest(String url, HttpMethod method, HttpEntity<?> entity,
                                                Class<T> responseType) {
    try {
      return restTemplate.exchange(url, method, entity, responseType);
    } catch (HttpStatusCodeException ex) {
      HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
      log.error(
          "Keycloak HTTP error: method={}, url={}, status={}, body={}",
          method, url, status, ex.getResponseBodyAsString()
      );
      switch (status) {
        case null -> throw new NullStatusException(NULL_HTTP_STATUS_CODE.getCode());
        case UNAUTHORIZED -> throw new UnauthorizedException(UNAUTHORIZED_ACCESS.getCode());
        case FORBIDDEN -> throw new ForbiddenException(FORBIDDEN.getCode());
        case NOT_FOUND -> throw new NotFoundException(URL_NOT_FOUND.getCode(), url);
        case BAD_REQUEST ->
            throw new BadRequestException(BAD_REQUEST.getCode(), ex.getResponseBodyAsString());
        case CONFLICT -> throw new ConflictException(CONFLICT_ERROR.getCode());
        default -> throw new UncaughtException(UNCAUGHT_ERROR.getCode(), status);
      }
    } catch (RestClientException ex) {
      throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), url, ex.getMessage());
    }
  }

  public static Map<String, Object> buildPayload(CreateKeycloakUser request) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(EMAIL, request.email());
    payload.put("firstName", request.name());
    payload.put("lastName", request.surname());
    payload.put(ENABLED, true);
    payload.put("emailVerified", true);
    payload.put("credentials", List.of(
        Map.of(
            "type", "password",
            "value", request.passwordTemp(),
            "temporary", true
        )
    ));
    return payload;
  }

  public static Map<String, Object> buildPayload(UpdateKeycloakUser request) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(EMAIL, request.email());
    return payload;
  }

  public static Map<String, Object> buildPayload(Boolean isEnabled) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(ENABLED, isEnabled);
    return payload;
  }
}
