package it.asansonne.common.graphql.jpa.kc.csr.service.impl;

import static it.asansonne.common.graphql.jpa.kc.enums.ErrorMessage.GROUP_NOT_FOUND;
import static it.asansonne.common.graphql.jpa.kc.enums.ErrorMessage.USER_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.graphql.jpa.kc.csr.repository.GroupRepository;
import it.asansonne.common.graphql.jpa.kc.csr.repository.specification.GroupSpecifications;
import it.asansonne.common.graphql.jpa.kc.csr.service.GroupService;
import it.asansonne.common.graphql.jpa.kc.dto.input.FilterGroup;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;
import java.security.Principal;
import java.util.UUID;
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
  private GroupSpecifications s;

  @Override
  public GroupModel findByRole(String name) {
    return repository.findByRole(name)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }

  @Override
  public GroupModel create(Principal principal, GroupModel model) {
    return repository.saveAndFlush(model);
  }

  @Override
  public Boolean deleteByUuid(Principal principal, GroupModel model) {
    if (repository.existsByUuid(model.getUuid())) {
      repository.delete(model);
      return true;
    } else {
      throw new NotFoundException(GROUP_NOT_FOUND.getCode());}
  }

  @Override
  public GroupModel findByUuid(Principal principal, UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }

  @Override
  public Page<GroupModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return repository.findByIsActive(isActive, pageable);
  }

  @Override
  public Page<GroupModel> findAll(Principal principal, FilterGroup filter, Pageable pageable) {
    return repository.findAll(s.withFilter(filter), pageable);
  }

  @Override
  public GroupModel update(Principal principal, GroupModel model) {
    if (repository.existsByUuid(model.getUuid())) {
      return repository.saveAndFlush(model);
    } else {
      throw new NotFoundException(USER_NOT_FOUND.getCode());
    }
  }

}
