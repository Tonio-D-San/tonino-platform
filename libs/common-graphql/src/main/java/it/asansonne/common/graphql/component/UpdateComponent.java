package it.asansonne.common.graphql.component;

import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Update;
import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface UpdateComponent<O extends Dto, U extends Update> {
  O updateByUuid(Principal principal, UUID uuid, U input);
}
