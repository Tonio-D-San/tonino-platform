package it.asansonne.common.keycloak.mapper;

import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;

@SuppressWarnings("unused")
public interface KcMapper<D extends Dto> {
  KcUser toKeycloakUser(D dto);

  D fromKeycloakUser(KcUser user);

  CreateKcUser toCreateKcUser(D dto);

  D fromCreateKcUser(CreateKcUser user);

  UpdateKcUser toUpdateKcUser(D dto);

  D fromUpdateKcUser(UpdateKcUser user);
}
