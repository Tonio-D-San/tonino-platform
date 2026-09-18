package it.asansonne.common.graphql.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import java.security.Principal;

@SuppressWarnings("unused")
public interface CreateComponent<I extends Create, O extends Dto> {
  O create(Principal principal, I input);
}
