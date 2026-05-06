package it.asansonne.peopleservice.ccsr.repository;

import it.asansonne.peopleservice.model.UserModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The interface Business user repository.
 */
@Repository
public interface UserRepository extends
    JpaRepository<UserModel, Long>,
    JpaSpecificationExecutor<UserModel> {
  /**
   * Find by uuid optional.
   *
   * @param uuid the uuid
   * @return the optional
   */
  Optional<UserModel> findByUuid(UUID uuid);

  /**
   * Find by email optional.
   *
   * @param email the email
   * @return the optional
   */
  Optional<UserModel> findByEmail(String email);
}
