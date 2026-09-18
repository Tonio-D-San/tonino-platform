package it.asansonne.common.graphql.jpa.kc.csr.component.impl;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.exception.DataIntegrityException;
import it.asansonne.common.graphql.jpa.kc.csr.component.UserComponent;
import it.asansonne.common.graphql.jpa.kc.csr.service.GroupService;
import it.asansonne.common.graphql.jpa.kc.csr.service.UserService;
import it.asansonne.common.graphql.jpa.kc.dto.input.CreateUser;
import it.asansonne.common.graphql.jpa.kc.dto.input.FilterUser;
import it.asansonne.common.graphql.jpa.kc.dto.input.UpdateUser;
import it.asansonne.common.graphql.jpa.kc.dto.output.User;
import it.asansonne.common.graphql.jpa.kc.mapper.UserMapper;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
import it.asansonne.common.graphql.jpa.kc.mapper.KeycloakMapper;
import java.security.Principal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
  private final KcComponent keycloakComponent;
  private final KeycloakMapper keycloakMapper;
  private final GroupService groupService;

  @Override
  public User userByEmail(Principal principal, String email) {
    return mapper.toDto(service.userByEmail(principal, email));
  }

  @Override
  public User create(Principal principal, CreateUser input) {
    GroupModel group = groupService.findByRole(input.role());
    UserModel model = keycloakMapper.toUser(
        keycloakComponent.createKeycloakUser(keycloakMapper.toKeycloakUser(input, group.getUuid())),
        input, group
    );
    model.setGroup(group);
    try {
      return mapper.toDto(service.create(principal, model));
    } catch (DataIntegrityViolationException e) {
      keycloakComponent.deleteKeycloakUser(model.getUuid());
      log.error("Errore durante la creazione dell'utente {}", model.getEmail(), e);
      throw new DataIntegrityException(BAD_REQUEST.getCode(), model.getEmail());
    }
  }

  @Override
  public Boolean deleteByUuid(Principal principal, UUID uuid) {
    return false;
  }

  @Override
  public User findByUuid(Principal principal, UUID uuid) {
    return null;
  }

  @Override
  public OutputPage<User> findByIsActive(Principal principal, Boolean isActive, Pageable pageable) {
    return null;
  }

  @Override
  public OutputPage<User> findAll(Principal principal, FilterUser filter, Pageable pageable) {
    return null;
  }

  @Override
  public User updateByUuid(Principal principal, UUID uuid, UpdateUser input) {
    return null;
  }
}
