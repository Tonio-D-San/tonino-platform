package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Update;
import it.asansonne.common.rest.dto.Response;
import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface PatchComponent<U extends Update, R extends Response> {
  R updateByUuid(Principal principal, UUID uuid, U update);
}
