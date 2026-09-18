package it.asansonne.common.graphql.jpa.kc.csr.repository;

import it.asansonne.common.jpa.repository.FindRepository;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * The interface Business user repository.
 */
@Repository
public interface UserRepository extends FindRepository<UserModel> {

  /**
   * Find by email.
   *
   * @param email the email
   * @return the optional
   */
  Optional<UserModel> findByEmail(String email);

}
