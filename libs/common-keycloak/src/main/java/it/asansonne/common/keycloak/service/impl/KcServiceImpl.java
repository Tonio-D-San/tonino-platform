package it.asansonne.common.keycloak.service.impl;

import static it.asansonne.common.core.enums.ErrorMessage.JWT_ERROR;
import static it.asansonne.common.keycloak.enums.ErrorMessage.KEYCLOAK_CALL_ERROR;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.EMAIL;
import static it.asansonne.common.keycloak.utils.RestCall.buildPayload;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.exception.DataIntegrityException;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KcService;
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
public class KcServiceImpl implements KcService {

  @Value("${keycloak.host.user}")
  private String urlUser;

  private final RestCall restCall;

  @Override
  public KcUser findByUuid(UUID uuid) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).queryParam("id", uuid).toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(this.buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KcUser findByEmail(String email) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).queryParam(EMAIL.getKey(), email).toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(this.buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Page<KcUser> findAll(Pageable pageable) {
    ResponseEntity<KcUser[]> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(buildHeaders()),
        KcUser[].class
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
  public KcUser createKeycloakUser(CreateKcUser request) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser).toUriString(),
        HttpMethod.POST,
        new HttpEntity<>(buildPayload(request), buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.CREATED) {
      log.info("Utente creato con successo su keycloak");
      return this.findByEmail(request.email());
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KcUser updateKeycloakUser(UUID uuid, UpdateKcUser request) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(request), buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente aggiornato con successo su keycloak");
      return this.findByEmail(request.email());
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean deleteKeycloakUser(UUID uuid) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.DELETE,
        new HttpEntity<>(buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente eliminato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(uuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(isEnabled), buildHeaders()),
        KcUser.class
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente disabilitato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public void addUserToGroup(UUID userUuid, UUID groupUuid) {
    ResponseEntity<KcUser> response = restCall.doRequest(
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(userUuid.toString(), "groups", groupUuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(buildHeaders()),
        KcUser.class
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
