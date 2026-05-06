package it.asansonne.common.keycloak.service.impl;

import static it.asansonne.common.core.enums.ErrorMessage.JWT_ERROR;
import static it.asansonne.common.keycloak.enums.ErrorMessage.KEYCLOAK_CALL_ERROR;
import static it.asansonne.common.keycloak.utils.RestCall.EMAIL;
import static it.asansonne.common.keycloak.utils.RestCall.buildPayload;

import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import it.asansonne.common.keycloak.exception.DataIntegrityException;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KeycloakService;
import it.asansonne.common.keycloak.utils.RestCall;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Keycloak service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

  @Value("${keycloak.host.user}")
  private String urlUser;

  private final RestCall restCall;

  @Override
  public KeycloakUser findByUuid(UUID uuid) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).queryParam("id", uuid).toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(this.buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KeycloakUser findByEmail(String email) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).queryParam(EMAIL, email).toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(this.buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Page<KeycloakUser> findAll(Pageable pageable) {
    ResponseEntity<KeycloakUser[]> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(buildHeaders()),
        KeycloakUser[].class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getBody() == null) {
      log.info("Nessun utente trovato su Keycloak");
      return Page.empty(pageable);
    }
    return new PageImpl<>(
        Arrays.asList(response.getBody()),
        pageable,
        countUsers()
    );
  }

  @Override
  public KeycloakUser createKeycloakUser(CreateKeycloakUser request) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).toUriString(),
        HttpMethod.POST,
        new HttpEntity<>(buildPayload(request), buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.CREATED) {
      log.info("Utente creato con successo su keycloak");
      return this.findByEmail(request.email());
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KeycloakUser updateKeycloakUser(UUID uuid, UpdateKeycloakUser request) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(request), buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente aggiornato con successo su keycloak");
      return this.findByEmail(request.email());
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean deleteKeycloakUser(UUID uuid) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.DELETE,
        new HttpEntity<>(buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente eliminato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(isEnabled), buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente disabilitato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public void addUserToGroup(UUID userUuid, UUID groupUuid) {
    ResponseEntity<KeycloakUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(userUuid.toString(), "groups", groupUuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildHeaders()),
        KeycloakUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente aggiunto al gruppo con successo su keycloak");
      return;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  private HttpHeaders buildHeaders() {
    if (
        !(SecurityContextHolder.getContext().getAuthentication()
            instanceof JwtAuthenticationToken jwtAuthToken)
    ) {
      throw new DataIntegrityException(JWT_ERROR.getCode());
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwtAuthToken.getToken().getTokenValue());
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  private long countUsers() {
    ResponseEntity<Integer> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment("count").build()
            .toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(buildHeaders()),
        Integer.class
    );
    return response.getBody() != null ? response.getBody() : 0L;
  }
}
