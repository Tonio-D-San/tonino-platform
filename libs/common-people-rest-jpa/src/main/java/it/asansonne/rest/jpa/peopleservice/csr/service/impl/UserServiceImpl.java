package it.asansonne.rest.jpa.peopleservice.csr.service.impl;

import static it.asansonne.rest.jpa.peopleservice.enums.ErrorMessage.USER_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.rest.jpa.peopleservice.csr.repository.UserRepository;
import it.asansonne.rest.jpa.peopleservice.csr.repository.specification.UserSpecifications;
import it.asansonne.rest.jpa.peopleservice.csr.service.UserService;
import it.asansonne.rest.jpa.peopleservice.dto.request.FilterUser;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type of Business user service.
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private UserSpecifications specifications;

  @Override
  public UserModel me(Principal principal) {
    return this.findByUuid(UUID.fromString(principal.getName()));
  }

  @Override
  public UserModel findByName(Principal principal, String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode(), name));
  }

  @Override
  public UserModel findBySurname(Principal principal, String surname) {
    return repository.findBySurname(surname)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode(), surname));
  }

  @Override
  public UserModel findByEmail(Principal principal, String email) {
    return repository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode(), email));
  }

  @Override
  public UserModel create(Principal principal, UserModel model) {
    return repository.save(model);
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UserModel model) {
    repository.delete(model);
    return true;
  }

  @Override
  public UserModel findByUuid(Principal principal, UUID uuid) {
    return this.findByUuid(uuid);
  }

  @Override
  public Page<UserModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return repository.findByIsActive(isActive, pageable);
  }

  @Override
  public Page<UserModel> findAll(Principal principal, FilterUser filter, Pageable pageable) {
    return repository.findAll(specifications.withFilter(filter), pageable);
  }

  @Override
  public UserModel update(Principal principal, UserModel model) {
    return repository.saveAndFlush(model);
  }

  private UserModel findByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode(), uuid.toString()));
  }
}
