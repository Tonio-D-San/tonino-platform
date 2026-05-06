package it.asansonne.peopleservice.ccsr.controller.impl;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.peopleservice.ccsr.component.UserComponent;
import it.asansonne.peopleservice.ccsr.controller.UserController;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.dto.input.UpdateUser;
import it.asansonne.peopleservice.dto.output.User;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * The type User controller.
 */
@Controller
@AllArgsConstructor
public class UserControllerImpl implements UserController {

  private final UserComponent component;

  @Override
  @MutationMapping(name = "createUser")
  public User create(Principal principal, @Argument CreateUser input) {
    return null;
  }

  @Override
  @MutationMapping(name = "deleteUser")
  public Boolean deleteByUuid(Principal principal, @Argument UUID uuid, @Argument Boolean delete) {
    return false;
  }

  @Override
  @QueryMapping(name = "userByUuid")
  public User findByUuid(Principal principal, @Argument UUID uuid) {
    return null;
  }

  @Override
  public OutputPage<User> findByIsActive(Principal principal, Boolean isActive) {
    return null;
  }

  @Override
  public OutputPage<User> findAll(Principal principal, FilterUser filter, Integer page,
                                  Integer size, String direction) {
    return null;
  }

  @Override
  public User updateByUuid(Principal principal, UUID uuid, UpdateUser input) {
    return null;
  }

  @Override
  public User userByEmail(Principal principal, String email) {
    return component.userByEmail(principal, email);
  }

}
