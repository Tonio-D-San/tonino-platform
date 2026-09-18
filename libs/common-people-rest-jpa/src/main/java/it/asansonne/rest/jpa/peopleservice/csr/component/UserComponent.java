package it.asansonne.rest.jpa.peopleservice.csr.component;

import it.asansonne.common.rest.component.CrudComponent;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
import java.security.Principal;

/**
 * The interface Business user component.
 */
@SuppressWarnings("unused")
public interface UserComponent extends CrudComponent<CreateUser, UpdateUser, FilterUser, User> {
  /**
   * Me user model.
   *
   * @param principal the principal
   * @return the user
   */
  User me(Principal principal);

  /**
   * Find by name user.
   *
   * @param principal the principal
   * @param name      the name
   * @return the user
   */
  User findByName(Principal principal, String name);

  /**
   * Find by surname user.
   *
   * @param principal the principal
   * @param surname   the surname
   * @return the user
   */
  User findBySurname(Principal principal, String surname);

  /**
   * Find by email user.
   *
   * @param principal the principal
   * @param email     the email
   * @return the user
   */
  User findByEmail(Principal principal, String email);
}
