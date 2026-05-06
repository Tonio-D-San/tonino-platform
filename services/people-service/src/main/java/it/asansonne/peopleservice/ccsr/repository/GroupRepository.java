package it.asansonne.peopleservice.ccsr.repository;

import it.asansonne.peopleservice.enums.UserRole;
import it.asansonne.peopleservice.model.GroupModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Business user repository.
 */
@Repository
public interface GroupRepository extends JpaRepository<GroupModel, Long> {

  /**
   * Find by name optional.
   *
   * @param role the name
   * @return the optional
   */
  Optional<GroupModel> findByRole(UserRole role);

  /**
   * Find by uuid optional.
   *
   * @param uuid the uuid
   * @return the optional
   */
  Optional<GroupModel> findByUuid(UUID uuid);
}
