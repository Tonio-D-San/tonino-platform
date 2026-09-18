package it.asansonne.rest.jpa.peopleservice.csr.component.impl;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.common.keycloak.service.KcService;
import it.asansonne.rest.jpa.peopleservice.csr.component.UserComponent;
import it.asansonne.rest.jpa.peopleservice.csr.service.GroupService;
import it.asansonne.rest.jpa.peopleservice.csr.service.UserService;
import it.asansonne.rest.jpa.peopleservice.dto.request.CreateUser;
import it.asansonne.rest.jpa.peopleservice.dto.request.FilterUser;
import it.asansonne.rest.jpa.peopleservice.dto.request.UpdateUser;
import it.asansonne.rest.jpa.peopleservice.dto.response.User;
import it.asansonne.rest.jpa.peopleservice.mapper.GroupMapper;
import it.asansonne.rest.jpa.peopleservice.mapper.KeycloakMapper;
import it.asansonne.rest.jpa.peopleservice.mapper.UserMapper;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final KcService keycloakComponent;
  private final KeycloakMapper keycloakMapper;
  private final GroupService groupService;
  private final GroupMapper groupMapper;

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
    if (Boolean.TRUE.equals(keycloakComponent.deleteKeycloakUser(uuid))) {
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
    KcUser kcUser = keycloakComponent.createKeycloakUser(CreateKcUser.builder()
        .name(request.name())
        .surname(request.surname())
        .email(request.email())
        .passwordTemp("")
        .groupUuid(group.getUuid())
        .build());
    return mapper.toDto(UserModel.builder()
        .name(kcUser.firstName())
        .surname(kcUser.lastName())
        .email(kcUser.email())
        .phoneNumber(request.phoneNumber())
        .group(group)
        .uuid(kcUser.id())
        .build()
    );
  }

  private UserModel findModelByUuid(Principal principal, UUID uuid) {
    return service.findByUuid(principal, uuid);
  }
}
