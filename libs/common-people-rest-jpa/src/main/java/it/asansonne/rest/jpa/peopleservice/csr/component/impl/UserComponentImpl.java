package it.asansonne.rest.jpa.peopleservice.csr.component.impl;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.core.exception.custom.DataIntegrityException;
import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
import it.asansonne.rest.jpa.peopleservice.csr.component.UserComponent;
import it.asansonne.rest.jpa.peopleservice.csr.service.GroupService;
import it.asansonne.rest.jpa.peopleservice.csr.service.UserService;
import it.asansonne.rest.jpa.peopleservice.mapper.UserMapper;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * The type Business user component.
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserComponentImpl implements UserComponent {
  private final UserService service;
  private final UserMapper mapper;
  private final KcComponent kcComponent;
  private final GroupService groupService;

  @Override
  public User me(Principal principal) {
    return mapper.toDto(service.me(principal));
  }

  @Override
  public User findByName(Principal principal, String name) {
    return mapper.toDto(service.findByName(principal, name));
  }

  @Override
  public User findBySurname(Principal principal, String surname) {
    return mapper.toDto(service.findBySurname(principal, surname));
  }

  @Override
  public User findByEmail(Principal principal, String email) {
    return mapper.toDto(service.findByEmail(principal, email));
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    UserModel model = this.findModelByUuid(principal, uuid);
    if (Boolean.TRUE.equals(kcComponent.deleteKcUser(uuid))) {
      return service.deleteByUuid(principal, model);
    }
    throw new KeycloakCallException("Eliminazione non riuscita"); //TODO: creare enum
  }

  @Override
  public User findByUuid(Principal principal, UUID uuid) {
    return mapper.toDto(this.findModelByUuid(principal, uuid));
  }

  @Override
  public Page<User> findByIsActive(Principal principal, Pageable pageable, Boolean isActive) {
    return mapper.toDto(service.findByIsActive(principal, isActive, pageable));
  }

  @Override
  public Page<User> findAll(Principal principal, FilterUser filter, Pageable pageable) {
    return mapper.toDto(service.findAll(principal, filter, pageable));
  }

  @Override
  public User updateByUuid(Principal principal, UUID uuid, UpdateUser update) {
    return null;
  }

  @Override
  public User create(Principal principal, CreateUser request) {
    GroupModel group = groupService.findByRole(principal, request.role());
    KcUser kcUser = kcComponent.createKcUser(CreateKcUser.builder()
        .name(request.name())
        .surname(request.surname())
        .email(request.email())
        .passwordTemp(request.pswTemp())
        .groupUuid(group.getUuid())
        .build());
    UUID userUuid = kcUser.id();
    String userEmail = kcUser.email();
    try {
      return mapper.toDto(service.create(principal, UserModel.builder()
          .name(kcUser.firstName())
          .surname(kcUser.lastName())
          .email(userEmail)
          .phoneNumber(request.phoneNumber())
          .group(group)
          .uuid(userUuid)
          .build()));
    } catch (DataIntegrityViolationException e) {
      kcComponent.deleteKcUser(userUuid);
      log.error("Errore durante la creazione dell'utente {}", userEmail, e);
      throw new DataIntegrityException(BAD_REQUEST.getCode(), userEmail);
    }
  }

  private UserModel findModelByUuid(Principal principal, UUID uuid) {
    return service.findByUuid(principal, uuid);
  }
}
