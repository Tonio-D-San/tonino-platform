package it.asansonne.peopleservice.ccsr.controller;

import it.asansonne.common.graphql.controller.QueryController;
import it.asansonne.common.graphql.controller.MutationController;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.dto.input.UpdateUser;
import it.asansonne.peopleservice.dto.output.User;
import java.security.Principal;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

/**
 * The type User controller.
 */
public interface UserController extends
    QueryController<User, FilterUser>,
    MutationController<User, CreateUser, UpdateUser>{

  /**
   * User by email User.
   *
   * @param principal the principal
   * @param email     the email
   * @return the user
   */
  @QueryMapping(name = "userByEmail")
  User userByEmail(Principal principal, @Argument String email);

}
