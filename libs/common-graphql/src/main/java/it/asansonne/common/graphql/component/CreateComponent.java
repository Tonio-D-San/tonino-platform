package it.asansonne.common.graphql.component;

import it.asansonne.common.graphql.dto.input.Create;
import it.asansonne.common.graphql.dto.output.Output;
import java.security.Principal;

@SuppressWarnings("unused")
public interface CreateComponent<I extends Create, O extends Output> {
  O create(Principal principal, I input);
}
