package it.asansonne.peopleservice.ccsr.service.impl;

import it.asansonne.peopleservice.ccsr.repository.UserRepository;
import it.asansonne.peopleservice.ccsr.service.UserService;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.model.UserModel;
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

  @Override
  public UserModel create(Principal principal, UserModel model) {
    return null;
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    return false;
  }

  @Override
  public UserModel findByUuid(Principal principal, UUID uuid) {
    return null;
  }

  @Override
  public Page<UserModel> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return null;
  }

  @Override
  public Page<UserModel> findAll(Principal principal, FilterUser filter, Pageable pageable) {
    return null;
  }

  @Override
  public UserModel update(Principal principal, UserModel model) {
    return null;
  }

  @Override
  public UserModel userByEmail(Principal principal, String email) {
    return null;
  }
}
