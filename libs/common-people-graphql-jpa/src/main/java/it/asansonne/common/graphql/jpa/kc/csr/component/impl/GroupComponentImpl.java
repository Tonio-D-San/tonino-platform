package it.asansonne.common.graphql.jpa.kc.csr.component.impl;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.jpa.kc.csr.component.GroupComponent;
import it.asansonne.common.graphql.jpa.kc.csr.service.GroupService;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.common.graphql.jpa.kc.mapper.GroupMapper;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * The type Group component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupComponentImpl implements GroupComponent {

  private final GroupMapper mapper;
  private final GroupService service;

  @Override
  public Group findByRole(String name) {
    log.debug("Searching for group with role {}", name);
    return mapper.toDto(service.findByRole(name));
  }

  @Override
  public Group create(Principal principal, CreateGroup input) {
    return mapper.toDto(service.create(principal, mapper.toModel(input)));
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    return false;
//    return service.deleteByUuid(principal, uuid); //TODO: implementare
  }

  @Override
  public Group findByUuid(Principal principal, UUID uuid) {
    return mapper.toDto(service.findByUuid(principal, uuid));
  }

  @Override
  public OutputPage<Group> findByIsActive(Principal principal, Boolean isActive,
                                          Pageable pageable) {
    return mapper.toPage(service.findByIsActive(principal, isActive, pageable));
  }

  @Override
  public OutputPage<Group> findAll(Principal principal, FilterGroup filter, Pageable pageable) {
    return mapper.toPage(service.findAll(principal, filter, pageable));
  }

  @Override
  public Group updateByUuid(Principal principal, UUID uuid, UpdateGroup input) {
    return mapper.toDto(service.update(principal, mapper.toModel(input)));
  }
}
