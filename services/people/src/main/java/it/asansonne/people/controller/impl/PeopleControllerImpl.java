package it.asansonne.people.controller.impl;

import it.asansonne.common.people.dto.response.User;
import it.asansonne.people.controller.PeopleController;
import it.asansonne.rest.jpa.peopleservice.csr.component.UserComponent;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PeopleControllerImpl implements PeopleController {
  private final UserComponent userComponent;
  @Override
  public User findPersonByUuid(Principal principal, UUID uuid) {
    return userComponent.findByUuid(principal, uuid);
  }
}
