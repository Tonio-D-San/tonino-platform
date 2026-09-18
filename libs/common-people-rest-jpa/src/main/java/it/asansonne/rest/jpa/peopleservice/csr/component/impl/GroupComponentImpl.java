package it.asansonne.rest.jpa.peopleservice.csr.component.impl;

import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.rest.jpa.peopleservice.csr.component.GroupComponent;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * The type Group component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupComponentImpl implements GroupComponent {

  @Override
  public Group findByRole(Principal principal, String name) {
    return null;
  }

  @Override
  public Group findByPath(Principal principal, String path) {
    return null;
  }

  @Override
  public Group findByDescription(Principal principal, String description) {
    return null;
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    return false;
  }

  @Override
  public Group findByUuid(Principal principal, UUID uuid) {
    return null;
  }

  @Override
  public Page<Group> findByIsActive(Principal principal, Pageable pageable, Boolean isActive) {
    return null;
  }

  @Override
  public Page<Group> findAll(Principal principal, FilterGroup filter, Pageable pageable) {
    return null;
  }

  @Override
  public Group updateByUuid(Principal principal, UUID uuid, UpdateGroup update) {
    return null;
  }

  @Override
  public Group create(Principal principal, CreateGroup request) {
    return null;
  }
}
