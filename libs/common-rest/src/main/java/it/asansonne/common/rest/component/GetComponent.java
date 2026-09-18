package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.rest.dto.Response;
import java.security.Principal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface GetComponent<F extends Filter, S extends Response> {
  S findByUuid(Principal principal, UUID uuid);

  Page<S> findByIsActive(Principal principal, Pageable pageable, Boolean isActive);

  Page<S> findAll(Principal principal, F filter, Pageable pageable);

}
