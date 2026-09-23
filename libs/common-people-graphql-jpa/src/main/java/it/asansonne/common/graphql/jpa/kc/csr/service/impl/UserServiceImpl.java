package it.asansonne.common.graphql.jpa.kc.csr.service.impl;

import static it.asansonne.common.graphql.jpa.kc.enums.ErrorMessage.USER_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.graphql.jpa.kc.csr.repository.UserRepository;
import it.asansonne.common.graphql.jpa.kc.csr.repository.specification.UserSpecifications;
import it.asansonne.common.graphql.jpa.kc.csr.service.UserService;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
import it.asansonne.common.people.dto.request.FilterUser;
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
  private UserSpecifications s;

  @Override
  public UserModel userByEmail(Principal principal, String email) {
    return repository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode()));
  }

  @Override
  public UserModel create(Principal principal, UserModel model) {
    return repository.saveAndFlush(model);
  }

  @Override
  public UserModel findByUuid(Principal principal, UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode()));
  }

  @Override
  public Page<UserModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return repository.findByIsActive(isActive, pageable);
  }

  @Override
  public Page<UserModel> findAll(Principal principal, FilterUser filter, Pageable pageable) {
    return repository.findAll(s.withFilter(filter), pageable);
  }

  @Override
  public UserModel update(Principal principal, UserModel model) {
    if (repository.existsByUuid(model.getUuid())) {
      return repository.saveAndFlush(model);
    } else {
      throw new NotFoundException(USER_NOT_FOUND.getCode());
    }
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UserModel model) {
    return false;
  }
}
