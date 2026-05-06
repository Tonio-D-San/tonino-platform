package it.asansonne.peopleservice.ccsr.component.impl;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.peopleservice.ccsr.component.GroupComponent;
import it.asansonne.peopleservice.ccsr.service.GroupService;
import it.asansonne.peopleservice.dto.output.Group;
import it.asansonne.peopleservice.enums.UserRole;
import it.asansonne.peopleservice.mapper.GroupMapper;
import it.asansonne.peopleservice.model.GroupModel;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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

  @Value("${group.admin-group-id}")
  private UUID adminGroupId;
  @Value("${group.operator-group-id}")
  private UUID operatorGroupId;
  @Value("${group.super-admin-group-id}")
  private UUID superAdminGroupId;

  /**
   * Init groups at startup.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void initGroupsAtStartup() {
    List<GroupModel> groups = service.syncGroups(List.of(
        GroupModel.builder()
            .uuid(adminGroupId)
            .role(UserRole.ADMIN)
            .path("/admin")
            .description(null)
            .build(),
        GroupModel.builder()
            .uuid(operatorGroupId)
            .role(UserRole.OPERATOR)
            .path("/operator")
            .description(null)
            .build(),
        GroupModel.builder()
            .uuid(superAdminGroupId)
            .role(UserRole.SUPER_USER)
            .path("/superadmin")
            .description(null)
            .build()
    ));
    log.info("Groups created: {}", groups);
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
  public OutputPage<Group> findAll(Principal principal, Filter filter, Pageable pageable) {
    return null;
  }
}
