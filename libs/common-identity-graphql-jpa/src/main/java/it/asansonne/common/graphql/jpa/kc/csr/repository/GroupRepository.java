package it.asansonne.common.graphql.jpa.kc.csr.repository;

import it.asansonne.common.jpa.repository.FindRepository;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * The interface Business user repository.
 */
@Repository
public interface GroupRepository extends FindRepository<GroupModel> {

  /**
   * Find by name optional.
   *
   * @param role the name
   * @return the optional
   */
  Optional<GroupModel> findByRole(String role);

}
