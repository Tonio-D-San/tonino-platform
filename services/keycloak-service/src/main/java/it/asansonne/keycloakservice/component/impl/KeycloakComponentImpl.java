package it.asansonne.keycloakservice.component.impl;

import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KeycloakService;
import it.asansonne.keycloakservice.component.KeycloakComponent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * The type Keycloak adapter component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakComponentImpl implements KeycloakComponent {

  private final KeycloakService service;

  public KeycloakUser findByUuid(UUID uuid) {
    return service.findByUuid(uuid);
  }

  public KeycloakUser findByEmail(String email) {
    return service.findByEmail(email);
  }

  public KeycloakUser createKeycloakUser(CreateKeycloakUser request) {
    KeycloakUser keycloakUser = service.createKeycloakUser(request);
    try {
      service.addUserToGroup(keycloakUser.id(), request.groupUuid());
    } catch (KeycloakCallException e) {
      log.error("Errore durante l'aggiunta dell'utente {} al gruppo {} su keycloak",
          keycloakUser.email(), request.groupUuid(), e);
      if(Boolean.TRUE.equals(this.deleteKeycloakUser(keycloakUser.id()))) {
        log.error("Utente {} eliminato con successo su keycloak", keycloakUser.email());
      }
      else {
        throw new KeycloakCallException("Errore durante l'aggiunta dell'utente al gruppo");
      }
    }
    return keycloakUser;
  }

  public KeycloakUser updateKeycloakUser(UUID uuid, UpdateKeycloakUser request) {
    return service.updateKeycloakUser(uuid, request);
  }

  public Boolean deleteKeycloakUser(UUID uuid) {
    return service.deleteKeycloakUser(uuid);
  }

  public Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled) {
    return service.disableKeycloakUser(uuid, isEnabled);
  }

  public Page<KeycloakUser> findAll(Pageable pageable) {
    return service.findAll(pageable);
  }

}
