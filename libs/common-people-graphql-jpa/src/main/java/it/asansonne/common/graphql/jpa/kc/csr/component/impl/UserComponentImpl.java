package it.asansonne.common.graphql.jpa.kc.csr.component.impl;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.core.exception.custom.DataIntegrityException;
import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.jpa.kc.csr.component.UserComponent;
import it.asansonne.common.graphql.jpa.kc.csr.service.GroupService;
import it.asansonne.common.graphql.jpa.kc.csr.service.UserService;
import it.asansonne.common.graphql.jpa.kc.mapper.UserMapper;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
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
  private final KcComponent kcComponent;
  private final GroupService groupService;

  @Override
  public User userByEmail(Principal principal, String email) {
    return mapper.toDto(service.userByEmail(principal, email));
  }

  @Override
  public User create(Principal principal, CreateUser input) {
    GroupModel group = groupService.findByRole(input.role());
    KcUser kcUser = kcComponent.createKcUser(
        CreateKcUser.builder()
            .name(input.name())
            .surname(input.surname())
            .email(input.email())
            .passwordTemp(input.pswTemp())
            .groupUuid(group.getUuid())
            .build()
    );
    try {
      return mapper.toDto(service.create(principal, UserModel.builder()
          .name(kcUser.firstName())
          .surname(kcUser.lastName())
          .email(kcUser.email())
          .phoneNumber(input.phoneNumber())
          .group(group)
          .uuid(kcUser.id())
          .build()));
    } catch (DataIntegrityViolationException e) {
      kcComponent.deleteKcUser(kcUser.id());
      log.error("Errore durante la creazione dell'utente {}", kcUser.email(), e);
      throw new DataIntegrityException(BAD_REQUEST.getCode(), kcUser.email());
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
