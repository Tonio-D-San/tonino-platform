package it.asansonne.common.graphql.component;

import it.asansonne.common.graphql.dto.input.Update;
import it.asansonne.common.graphql.dto.output.Output;
import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface UpdateComponent<O extends Output, U extends Update> {
  O updateByUuid(Principal principal, UUID uuid, U input);
}
