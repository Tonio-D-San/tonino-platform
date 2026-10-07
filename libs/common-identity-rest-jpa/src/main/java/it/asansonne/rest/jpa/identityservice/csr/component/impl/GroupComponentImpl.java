package it.asansonne.rest.jpa.identityservice.csr.component.impl;

import static it.asansonne.rest.jpa.identityservice.enums.ErrorMessage.GROUP_DATA_INTEGRITY;
import static it.asansonne.rest.jpa.identityservice.enums.ErrorMessage.GROUP_ROLE_DUPLICATE;

import it.asansonne.common.core.exception.custom.DataIntegrityException;
import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcGroup;
import it.asansonne.common.keycloak.dto.output.KcGroup;
import it.asansonne.common.identity.dto.request.CreateGroup;
import it.asansonne.common.identity.dto.request.FilterGroup;
import it.asansonne.common.identity.dto.request.UpdateGroup;
import it.asansonne.common.identity.dto.response.Group;
import it.asansonne.rest.jpa.identityservice.csr.component.GroupComponent;
import it.asansonne.rest.jpa.identityservice.csr.service.GroupService;
import it.asansonne.rest.jpa.identityservice.mapper.GroupMapper;
import it.asansonne.rest.jpa.identityservice.model.GroupModel;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    if (Boolean.TRUE.equals(kcComponent.deleteKcGroup(uuid))) {
      return service.deleteByUuid(principal, this.service.findByUuid(principal, uuid));
    }
    return false;
  }

  @Override
  public Group findByUuid(Principal principal, UUID uuid) {
    return mapper.toDto(service.findByUuid(principal, uuid));
  }

  @Override
  public Page<Group> findByIsActive(Principal principal, Pageable pageable, Boolean isActive) {
    return mapper.toDto(service.findByIsActive(principal, isActive, pageable));
  }

  @Override
  public Page<Group> findAll(Principal principal, FilterGroup filter, Pageable pageable) {
    return mapper.toDto(service.findAll(principal, filter, pageable));
  }

  @Override
  public Group updateByUuid(Principal principal, UUID uuid, UpdateGroup update) {
    return mapper.toDto(service.update(principal, mapper.updateToModel(update)));
  }

  @Override
  public Group create(Principal principal, CreateGroup request) {
    KcGroup kcGroup = kcComponent.createKcGroup(CreateKcGroup.builder()
        .name(request.role())
        .parentId(request.parentId())
        .attributes(Map.of(
            KcGroup.DESCRIPTION,
            List.of(request.description() == null ? "" : request.description())
        )).build()
    );
    UUID groupUuid = kcGroup.id();
    String groupName = kcGroup.name();
    try {
      return mapper.toDto(service.create(principal, GroupModel.builder()
          .role(groupName)
          .path(kcGroup.path())
          .description(kcGroup.getDescription())
          .users(null)
          .uuid(groupUuid)
          .build())
      );
    } catch (DataIntegrityViolationException e) {
      kcComponent.deleteKcGroup(groupUuid);
      log.error("Errore durante la creazione del gruppo {}", groupName, e);
      throw new DataIntegrityException(resolveGroupIntegrityError(e), groupName);
    }
  }

  private String resolveGroupIntegrityError(DataIntegrityViolationException exception) {
    String message = exception.getMostSpecificCause().getMessage();
    if (message == null) {
      return GROUP_DATA_INTEGRITY.getCode();
    }
    if (message.contains("role")) {
      return GROUP_ROLE_DUPLICATE.getCode();
    }
    return GROUP_DATA_INTEGRITY.getCode();
  }

}
