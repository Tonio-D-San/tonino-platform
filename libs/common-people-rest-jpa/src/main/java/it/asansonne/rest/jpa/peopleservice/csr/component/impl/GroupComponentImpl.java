package it.asansonne.rest.jpa.peopleservice.csr.component.impl;

import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcGroup;
import it.asansonne.common.keycloak.dto.output.KcGroup;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.rest.jpa.peopleservice.csr.component.GroupComponent;
import it.asansonne.rest.jpa.peopleservice.csr.service.GroupService;
import it.asansonne.rest.jpa.peopleservice.mapper.GroupMapper;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import java.security.Principal;
import java.util.List;
import java.util.Map;
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

  private final GroupService service;
  private final GroupMapper mapper;
  private final KcComponent kcComponent;

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
    String description = request.description() == null ? "" : request.description();
    KcGroup kcGroup = kcComponent.createKcGroup(CreateKcGroup.builder()
        .name(request.role())
        .parentId(request.parentId())
        .attributes(Map.of(KcGroup.DESCRIPTION, List.of(description)))
        .build()
    );
    return mapper.toDto(service.create(principal, GroupModel.builder()
        .role(kcGroup.name())
        .path(kcGroup.path())
        .description(kcGroup.getDescription())
        .users(null)
        .uuid(kcGroup.id())
        .build())
    );
  }
  
}
