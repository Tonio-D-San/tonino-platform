package it.asansonne.peopleservice.ccsr.component.impl;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.keycloakservice.component.KeycloakComponent;
import it.asansonne.peopleservice.ccsr.component.UserComponent;
import it.asansonne.peopleservice.ccsr.service.GroupService;
import it.asansonne.peopleservice.ccsr.service.UserService;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.dto.input.UpdateUser;
import it.asansonne.peopleservice.dto.output.User;
import it.asansonne.peopleservice.exception.custom.DataIntegrityException;
import it.asansonne.peopleservice.mapper.KeycloakMapper;
import it.asansonne.peopleservice.mapper.UserMapper;
import it.asansonne.peopleservice.model.GroupModel;
import it.asansonne.peopleservice.model.UserModel;
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
  private final KeycloakComponent keycloakComponent;
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
  public Boolean deleteByUuid(UUID uuid) {
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
