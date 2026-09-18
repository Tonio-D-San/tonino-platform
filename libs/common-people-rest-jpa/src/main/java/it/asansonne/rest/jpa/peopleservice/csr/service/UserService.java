package it.asansonne.rest.jpa.peopleservice.csr.service;

import it.asansonne.common.jpa.service.CrudService;
import it.asansonne.rest.jpa.peopleservice.dto.request.FilterUser;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import java.security.Principal;

/**
 * The interface User service.
 */
public interface UserService extends CrudService<UserModel, FilterUser> {
  /**
   * Me user model.
   *
   * @param principal the principal
   * @return the user model
   */
  UserModel me(Principal principal);

  /**
   * Find by name optional.
   *
   * @param principal the principal
   * @param name      the name
   * @return the optional
   */
  UserModel findByName(Principal principal, String name);

  /**
   * Find by surname optional.
   *
   * @param principal the principal
   * @param surname   the surname
   * @return the optional
   */
  UserModel findBySurname(Principal principal, String surname);

  /**
   * Find by email optional.
   *
   * @param principal the principal
   * @param email     the email
   * @return the optional
   */
  UserModel findByEmail(Principal principal, String email);

}
