package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import java.security.Principal;

@SuppressWarnings("unused")
public interface PostComponent<C extends Create, S extends Dto> {
  S create(Principal principal, C request);
}
