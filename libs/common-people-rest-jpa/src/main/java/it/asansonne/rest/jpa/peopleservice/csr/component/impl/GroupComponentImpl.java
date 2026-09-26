package it.asansonne.rest.jpa.peopleservice.csr.component.impl;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.keycloak.component.KcComponent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    return mapper.toDto(service.create(principal, mapper.createToModel(request)));
  }

  @Override
  @Transactional
  public List<Group> syncFromKeycloak() {
    List<Group> synced = new ArrayList<>();
    for (KcGroup kcGroup : kcComponent.findAllGroups()) {
      syncGroup(kcGroup, synced);
    }
    log.info("Sincronizzati {} gruppi da Keycloak", synced.size());
    return synced;
  }

  private void syncGroup(KcGroup kcGroup, List<Group> synced) {
    if (kcGroup == null || kcGroup.name() == null || kcGroup.name().isBlank()) {
      return;
    }
    GroupModel model;
    try {
      model = service.findByRole(null, kcGroup.name());
      model.setPath(kcGroup.path());
      model.setDescription(kcGroup.description());
      model.activate();
      model = service.update(null, model);
    } catch (NotFoundException _) {
      model = GroupModel.builder()
          .uuid(kcGroup.id())
          .role(kcGroup.name())
          .path(kcGroup.path())
          .description(kcGroup.description())
          .build();
      model = service.create(null, model);
    }
    synced.add(mapper.toLittleDto(model));

    if (kcGroup.subGroups() != null) {
      kcGroup.subGroups().forEach(subGroup -> syncGroup(subGroup, synced));
    }
  }
}
