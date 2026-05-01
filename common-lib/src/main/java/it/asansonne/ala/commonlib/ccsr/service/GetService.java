package it.asansonne.ala.commonlib.ccsr.service;

import it.asansonne.ala.commonlib.ccsr.model.BaseModel;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetService<M extends BaseModel> {

  Optional<M> findByUuid(UUID uuid);

  Page<M> findByIsActive(
      Pageable pageable,
      Boolean isActive
  );

  Page<M> findAll(
      Pageable pageable, Locale locale
  );

}
