package it.asansonne.common.jpa.service;

import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface DeleteService {
  Boolean deleteByUuid(Principal principal, UUID uuid);

}
