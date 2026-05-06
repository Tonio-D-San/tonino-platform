package it.asansonne.common.jpa.service;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.jpa.model.BaseModel;
import java.security.Principal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface FindService<M extends BaseModel, F extends Filter> {
  M findByUuid(Principal principal, UUID uuid);

  Page<M> findByIsActive(Principal principal, Boolean isActive, Pageable pageable);

  Page<M> findAll(Principal principal, F filter, Pageable pageable);

}
