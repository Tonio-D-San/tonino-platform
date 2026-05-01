package it.asansonne.ala.commonlib.ccsr.component;

import it.asansonne.ala.commonlib.dto.Request;
import it.asansonne.ala.commonlib.dto.Response;
import java.security.Principal;

public interface PostComponent<R extends Request, S extends Response> {

  S create(Principal principal, R request);
}
