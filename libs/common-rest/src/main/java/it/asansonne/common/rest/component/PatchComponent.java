package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Update;
import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface PatchComponent<U extends Update, R extends Dto> {
  R updateByUuid(Principal principal, UUID uuid, U update);
}
