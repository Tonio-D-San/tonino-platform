package it.asansonne.peopleservice.ccsr.component;

import it.asansonne.common.graphql.component.CreateComponent;
import it.asansonne.common.graphql.component.DeleteComponent;
import it.asansonne.common.graphql.component.FindComponent;
import it.asansonne.common.graphql.component.UpdateComponent;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.dto.input.UpdateUser;
import it.asansonne.peopleservice.dto.output.User;
import java.security.Principal;

/**
 * The interface Business user component.
 */
public interface UserComponent extends
    FindComponent<User, FilterUser>, CreateComponent<CreateUser, User>,
    UpdateComponent<User, UpdateUser>, DeleteComponent {
  User userByEmail(Principal principal, String email);
}
