package it.asansonne.peopleservice.ccsr.service.impl;

import static it.asansonne.common.core.enums.ErrorMessage.NOT_IMPLEMENTED;
import static it.asansonne.peopleservice.enums.ErrorMessage.GROUP_NOT_FOUND;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.peopleservice.ccsr.repository.GroupRepository;
import it.asansonne.peopleservice.ccsr.service.GroupService;
import it.asansonne.peopleservice.enums.UserRole;
import it.asansonne.peopleservice.model.GroupModel;
import jakarta.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type Group service.
 */
@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
  private final GroupRepository repository;

  @Override
  @Transactional
  public List<GroupModel> syncGroups(List<GroupModel> defaults) {
    Map<UserRole, GroupModel> existingByRole = repository.findAll().stream()
        .collect(Collectors.toMap(GroupModel::getRole, Function.identity()));
    return repository.saveAll(
        defaults.stream()
            .map(group -> {
              GroupModel existing = existingByRole.get(group.getRole());
              if (existing == null) {
                return group;
              }
              existing.setPath(group.getPath());
              existing.setDescription(group.getDescription());
              return existing;
            }).toList()
    );
  }

  @Override
  public GroupModel findByUuid(Principal principal, UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode(), uuid));
  }

  @Override
  public GroupModel findByRole(UserRole name) {
    return repository.findByRole(name)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode(), name));
  }

  @Override
  public Page<GroupModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    throw new OperationNotAllowedException(NOT_IMPLEMENTED.getCode());
  }

  @Override
  public Page<GroupModel> findAll(Principal principal, Filter filter, Pageable pageable) {
    throw new OperationNotAllowedException(NOT_IMPLEMENTED.getCode());
  }
}
