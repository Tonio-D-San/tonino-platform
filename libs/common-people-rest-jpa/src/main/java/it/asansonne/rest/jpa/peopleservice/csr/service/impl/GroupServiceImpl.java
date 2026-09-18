package it.asansonne.rest.jpa.peopleservice.csr.service.impl;

import static it.asansonne.rest.jpa.peopleservice.csr.repository.specification.GroupSpecifications.descriptionLike;
import static it.asansonne.rest.jpa.peopleservice.csr.repository.specification.GroupSpecifications.pathLike;
import static it.asansonne.rest.jpa.peopleservice.enums.ErrorMessage.GROUP_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.rest.jpa.peopleservice.csr.repository.GroupRepository;
import it.asansonne.rest.jpa.peopleservice.csr.repository.specification.GroupSpecifications;
import it.asansonne.rest.jpa.peopleservice.csr.service.GroupService;
import it.asansonne.rest.jpa.peopleservice.dto.request.FilterGroup;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
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
  private GroupSpecifications specifications;

  @Override
  public GroupModel findByRole(Principal principal, String role) {
    return repository.findByRole(role)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }

  @Override
  public GroupModel findByPath(Principal principal, String path) {
    return repository.findByPath(pathLike(path).toString())
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }

  @Override
  public GroupModel findByDescription(Principal principal, String description) {
    return repository.findByDescription(descriptionLike(description).toString())
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }

  @Override
  public GroupModel create(Principal principal, GroupModel model) {
    return repository.save(model);
  }

  @Override
  public Boolean deleteByUuid(Principal principal, GroupModel model) {
    repository.delete(model);
    return true;
  }

  @Override
  public GroupModel findByUuid(Principal principal, UUID uuid) {
    return this.findByUuid(uuid);
  }

  @Override
  public Page<GroupModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return repository.findByIsActive(isActive, pageable);
  }

  @Override
  public Page<GroupModel> findAll(Principal principal, FilterGroup filter, Pageable pageable) {
    return repository.findAll(specifications.withFilter(filter), pageable);
  }

  @Override
  public GroupModel update(Principal principal, GroupModel model) {
    return repository.saveAndFlush(model);
  }

  private GroupModel findByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND.getCode()));
  }
}
