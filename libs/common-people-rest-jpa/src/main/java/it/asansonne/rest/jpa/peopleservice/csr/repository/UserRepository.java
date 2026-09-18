package it.asansonne.rest.jpa.peopleservice.csr.repository;

import it.asansonne.common.jpa.repository.FindRepository;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * The interface Business user repository.
 */
@Repository
public interface UserRepository extends FindRepository<UserModel> {
  /**
   * Find by name optional.
   *
   * @param name the name
   * @return the optional
   */
  Optional<UserModel> findByName(String name);

  /**
   * Find by surname optional.
   *
   * @param surname the surname
   * @return the optional
   */
  Optional<UserModel> findBySurname(String surname);

  /**
   * Find by email.
   *
   * @param email the email
   * @return the optional
   */
  Optional<UserModel> findByEmail(String email);
}
