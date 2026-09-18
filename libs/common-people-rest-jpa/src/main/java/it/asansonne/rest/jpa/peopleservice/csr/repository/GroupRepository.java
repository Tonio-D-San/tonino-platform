package it.asansonne.rest.jpa.peopleservice.csr.repository;

import it.asansonne.common.jpa.repository.FindRepository;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
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

  /**
   * Find by path optional.
   *
   * @param path the path
   * @return the optional
   */
  Optional<GroupModel> findByPath(String path);

  /**
   * Find by description optional.
   *
   * @param description the description
   * @return the optional
   */
  Optional<GroupModel> findByDescription(String description);

  String role(String role);
}
