package it.asansonne.common.jpa.repository;

import it.asansonne.common.jpa.model.BaseModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@SuppressWarnings("unused")
@NoRepositoryBean
public interface FindRepository<M extends BaseModel> extends
    JpaRepository<M, Long>, JpaSpecificationExecutor<M>
{
  Optional<M> findByUuid(UUID uuid);

  Page<M> findByIsActive(Boolean isActive, Pageable pageable);

}
