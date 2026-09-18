package it.asansonne.common.keycloak.component.impl;

import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.service.KcService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KcComponentImpl implements KcComponent {

  private final KcService service;

  @Override
  public KcUser findByUuid(UUID uuid) {
    return service.findByUuid(uuid);
  }

  @Override
  public KcUser findByEmail(String email) {
    return service.findByEmail(email);
  }

  @Override
  public KcUser createKeycloakUser(CreateKcUser user) {
    return service.createKeycloakUser(user);
  }

  @Override
  public KcUser updateKeycloakUser(UUID uuid, UpdateKcUser user) {
    return service.updateKeycloakUser(uuid, user);
  }

  @Override
  public Boolean deleteKeycloakUser(UUID uuid) {
    return service.deleteKeycloakUser(uuid);
  }

  @Override
  public Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled) {
    return service.disableKeycloakUser(uuid, isEnabled);
  }

  @Override
  public Page<KcUser> findAll(Pageable pageable) {
    return service.findAll(pageable);
  }
}
