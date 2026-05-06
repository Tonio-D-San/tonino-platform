package it.asansonne.common.graphql.component;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.graphql.dto.output.Output;
import it.asansonne.common.graphql.dto.page.OutputPage;
import java.security.Principal;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface FindComponent<O extends Output, F extends Filter> {
  O findByUuid(Principal principal, UUID uuid);

  OutputPage<O> findByIsActive(Principal principal, Boolean isActive, Pageable pageable);

  OutputPage<O> findAll(Principal principal, F filter, Pageable pageable);
}
