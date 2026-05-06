package it.asansonne.common.rest.component;

import it.asansonne.common.rest.dto.Response;
import java.security.Principal;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface GetComponent<S extends Response> {
  S findByUuid(UUID uuid);

  Page<S> findByIsActive(
      Pageable pageable,
      Boolean isActive
  );

  Page<S> findAll(
      Pageable pageable,
      Locale locale, Principal principal
  );

}
