package it.asansonne.common.graphql.jpa.kc.csr.service;

import it.asansonne.common.jpa.service.CrudService;
import it.asansonne.common.graphql.jpa.kc.dto.input.FilterUser;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
import java.security.Principal;

/**
 * The interface User service.
 */
public interface UserService extends CrudService<UserModel, FilterUser> {

  UserModel userByEmail(Principal principal, String email);

}
