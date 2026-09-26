package it.asansonne.people.controller.impl;

import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
import it.asansonne.people.controller.PeopleController;
import it.asansonne.rest.jpa.peopleservice.csr.component.UserComponent;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}${api.resource.people}")
@AllArgsConstructor
public class PeopleControllerImpl implements PeopleController {
  private final UserComponent component;

  @Override
  public User findByUuid(Principal principal, UUID uuid) {
    return component.findByUuid(principal, uuid);
  }

  @Override
  public Page<User> findByIsActive(Principal principal, Boolean isActive, Integer page,
                                   Integer size, String direction) {
    return component.findByIsActive(
        principal, PageRequest.of(
            page == null ? 0 : page,
            size == null ? 20 : size,
            Sort.by(
                Sort.Direction.fromString(
                    direction == null || direction.isBlank() ? "ASC" : direction
                ), UPDATED_AT
            )
        ), isActive);
  }

  @Override
  public Page<User> findAll(Principal principal, FilterUser filter, Integer page, Integer size,
                            String direction) {
    return null;
  }

  @Override
  public void deleteByUuid(Principal principal, UUID uuid) {

  }

  @Override
  public void updateByUuid(UUID uuid, UpdateUser request) {

  }

  @Override
  public User create(Principal principal, CreateUser request) {
    return component.create(principal, request);
  }
}
