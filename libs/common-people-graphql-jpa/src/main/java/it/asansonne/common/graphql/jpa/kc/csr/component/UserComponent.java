package it.asansonne.common.graphql.jpa.kc.csr.component;

import it.asansonne.common.graphql.component.CrudComponent;
import it.asansonne.common.graphql.jpa.kc.dto.input.CreateUser;
import it.asansonne.common.graphql.jpa.kc.dto.input.FilterUser;
import it.asansonne.common.graphql.jpa.kc.dto.input.UpdateUser;
import it.asansonne.common.graphql.jpa.kc.dto.output.User;
import java.security.Principal;

/**
 * The interface Business user component.
 */
public interface UserComponent extends CrudComponent<User, CreateUser, FilterUser, UpdateUser> {
  User userByEmail(Principal principal, String email);
}
