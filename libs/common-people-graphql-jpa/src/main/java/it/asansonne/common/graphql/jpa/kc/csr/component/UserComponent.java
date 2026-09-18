package it.asansonne.common.graphql.jpa.kc.csr.component;

import it.asansonne.common.graphql.component.CrudComponent;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
import java.security.Principal;

/**
 * The interface Business user component.
 */
public interface UserComponent extends CrudComponent<User, CreateUser, FilterUser, UpdateUser> {
  User userByEmail(Principal principal, String email);
}
