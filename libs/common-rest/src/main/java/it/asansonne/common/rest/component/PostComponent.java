package it.asansonne.common.rest.component;

import it.asansonne.common.rest.dto.Request;
import it.asansonne.common.rest.dto.Response;
import java.security.Principal;

@SuppressWarnings("unused")
public interface PostComponent<R extends Request, S extends Response> {
  S create(Principal principal, R request);
}
