package it.asansonne.people.controller.impl;

import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.people.controller.GroupController;
import it.asansonne.rest.jpa.peopleservice.csr.component.GroupComponent;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/admin${api.resource.groups}")
@RequiredArgsConstructor
public class GroupControllerImpl implements GroupController {

  private final GroupComponent component;

  @Override
  public Group findByUuid(Principal principal, UUID uuid) {
    return component.findByUuid(principal, uuid);
  }

  @Override
  public Page<Group> findByIsActive(Principal principal, Boolean isActive, Integer page,
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
  public Page<Group> findAll(Principal principal, FilterGroup filter, Integer page, Integer size,
                             String direction) {
    return component.findAll(principal, filter, PageRequest.of(
        page == null ? 0 : page,
        size == null ? 20 : size,
        Sort.by(
            Sort.Direction.fromString(
                direction == null || direction.isBlank() ? "ASC" : direction
            ), UPDATED_AT
        )
    ));
  }

  @Override
  public Group create(Principal principal, CreateGroup request) {
    return component.create(principal, request);
  }

  @Override
  public void updateByUuid(Principal principal, UUID uuid, UpdateGroup request) {
    component.updateByUuid(principal, uuid, request);
  }

  @Override
  public void deleteByUuid(Principal principal, UUID uuid) {
    component.deleteByUuid(principal, uuid);
  }

}
