package it.asansonne.common.keycloak.service.impl;

import static it.asansonne.common.core.enums.ErrorMessage.URL_NOT_FOUND;
import static it.asansonne.common.keycloak.enums.ErrorMessage.KEYCLOAK_CALL_ERROR;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.EMAIL;
import static it.asansonne.common.keycloak.utils.RestCall.buildPayload;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KcService;
import it.asansonne.common.keycloak.utils.JwtRestHeadersProvider;
import it.asansonne.common.rest.exception.handler.RestErrorHandler;
import it.asansonne.common.rest.executor.RestClientExecutor;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public static final String KEYCLOAK = "KEYCLOAK";
  private final JwtRestHeadersProvider headersProvider;
  private final RestErrorHandler errorHandler;
  private final RestClientExecutor restClient;

  @Override
  public KcUser findByUuid(UUID uuid) {
    ResponseEntity<KcUser> response = restClient.exchange(
        KEYCLOAK,
        userUrl(uuid),
        HttpMethod.GET,
        new HttpEntity<>(headersProvider.build()),
        KcUser.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KcUser findByEmail(String email) {
    ResponseEntity<KcUser[]> response = restClient.exchange(
        KEYCLOAK,
        UriComponentsBuilder.fromUriString(urlUser)
            .queryParam(EMAIL.getKey(), email)
            .queryParam("exact", true)
            .toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(headersProvider.build()),
        KcUser[].class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.OK) {
      KcUser[] users = response.getBody();
      if (users != null && users.length > 0) {
        return users[0];
      }
      throw new NotFoundException(URL_NOT_FOUND.getCode(), email);
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Page<KcUser> findAll(Pageable pageable) {
    ResponseEntity<KcUser[]> response = restClient.exchange(
        KEYCLOAK,
        UriComponentsBuilder.fromUriString(urlUser)
            .queryParam("first", pageable.getOffset())
            .queryParam("max", pageable.getPageSize())
            .toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(headersProvider.build()),
        KcUser[].class, errorHandler
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
    ResponseEntity<Void> response = restClient.exchange(
        KEYCLOAK,
        UriComponentsBuilder.fromUriString(urlUser).toUriString(),
        HttpMethod.POST,
        new HttpEntity<>(buildPayload(request), headersProvider.build()),
        Void.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.CREATED) {
      log.info("Utente creato con successo su keycloak");
      return this.findByUuid(extractUserUuid(response.getHeaders().getLocation()));
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public KcUser updateKeycloakUser(UUID uuid, UpdateKcUser request) {
    ResponseEntity<Void> response = restClient.exchange(
        KEYCLOAK,
        userUrl(uuid),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(request), headersProvider.build()),
        Void.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente aggiornato con successo su keycloak");
      return this.findByUuid(uuid);
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean deleteKeycloakUser(UUID uuid) {
    ResponseEntity<Void> response = restClient.exchange(
        KEYCLOAK,
        userUrl(uuid),
        HttpMethod.DELETE,
        new HttpEntity<>(headersProvider.build()),
        Void.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente eliminato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled) {
    ResponseEntity<Void> response = restClient.exchange(
        KEYCLOAK,
        userUrl(uuid),
        HttpMethod.PUT,
        new HttpEntity<>(buildPayload(isEnabled), headersProvider.build()),
        Void.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Stato utente aggiornato con successo su keycloak");
      return true;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  @Override
  public void addUserToGroup(UUID userUuid, UUID groupUuid) {
    ResponseEntity<Void> response = restClient.exchange(
        KEYCLOAK,
        UriComponentsBuilder.fromUriString(urlUser)
            .pathSegment(userUuid.toString(), "groups", groupUuid.toString()).build()
            .toUriString(),
        HttpMethod.PUT,
        new HttpEntity<>(headersProvider.build()),
        Void.class, errorHandler
    );
    if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
      log.info("Utente aggiunto al gruppo con successo su keycloak");
      return;
    }
    throw new KeycloakCallException(KEYCLOAK_CALL_ERROR.getCode(), urlUser, response.getBody());
  }

  private long countUsers() {
    ResponseEntity<Integer> response = restClient.exchange(
        KEYCLOAK,
        UriComponentsBuilder.fromUriString(urlUser).pathSegment("count").build().toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(headersProvider.build()),
        Integer.class, errorHandler
    );
    return response.getBody() != null ? response.getBody() : 0L;
  }

  private String userUrl(UUID uuid) {
    return UriComponentsBuilder.fromUriString(urlUser)
        .pathSegment(uuid.toString())
        .build()
        .toUriString();
  }

  private UUID extractUserUuid(URI location) {
    if (location == null || location.getPath() == null || location.getPath().isBlank()) {
      throw new KeycloakCallException(
          KEYCLOAK_CALL_ERROR.getCode(),
          urlUser,
          "Missing Location header"
      );
    }
    String path = location.getPath();
    return UUID.fromString(path.substring(path.lastIndexOf('/') + 1));
  }
}
