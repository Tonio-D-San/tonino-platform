package it.asansonne.common.keycloak.component.impl;

import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcGroup;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcGroup;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KcService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KcComponentImpl implements KcComponent {

  private final KcService service;

  @Override
  public KcUser findByUuid(UUID uuid) {
    return service.findUserByUuid(uuid);
  }

  @Override
  public KcUser findByEmail(String email) {
    return service.findByEmail(email);
  }

  @Override
  public KcUser createKcUser(CreateKcUser user) {
    KcUser kcUser = service.createKcUser(user);
    try {
      service.addUserToGroup(kcUser.id(), user.groupUuid());
    } catch (KeycloakCallException ex) {
      log.error("Errore durante l'aggiunta dell'utente {} al gruppo {} su keycloak",
          kcUser.email(), user.groupUuid(), ex);
      this.deleteKcUser(kcUser.id());
      throw ex;
    }
    return kcUser;
  }

  @Override
  public KcGroup createKcGroup(CreateKcGroup group) {
    return service.createKcGroup(group);
  }

  @Override
  public KcUser updateKcUser(UUID uuid, UpdateKcUser user) {
    return service.updateKcUser(uuid, user);
  }

  @Override
  public Boolean deleteKcUser(UUID uuid) {
    return service.deleteKcUser(uuid);
  }
  @Override
  public Boolean deleteKcGroup(UUID uuid) {
    return service.deleteKcGroup(uuid);
  }

  @Override
  public Boolean disableKcUser(UUID uuid, Boolean isEnabled) {
    return service.disableKcUser(uuid, isEnabled);
  }

  @Override
  public Page<KcUser> findAllUsers(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  public List<KcGroup> findAllGroups() {
    return service.findAllGroups();
  }
}
