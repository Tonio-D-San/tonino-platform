package it.asansonne.keycloakservice.controller.impl;

import static it.asansonne.common.core.enums.ErrorMessage.NOT_IMPLEMENTED;

import io.swagger.v3.oas.annotations.Hidden;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.common.keycloak.component.KcComponent;
import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.keycloakservice.controller.KeycloakController;
import it.asansonne.keycloakservice.dto.request.CreateUserRequest;
import it.asansonne.keycloakservice.dto.request.UpdateUserRequest;
import it.asansonne.keycloakservice.dto.response.UserResponse;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class KeycloakControllerImpl implements KeycloakController {

  private final KcComponent component;

  @Override
  public void deleteByUuid(UUID uuid) {
    if (Boolean.TRUE.equals(component.deleteKeycloakUser(uuid))) {
      log.info("User {} deleted", uuid);
    } else {
      log.error("User {} not deleted", uuid);
      throw new KeycloakCallException("");
    }
  }

  @Override
  public UserResponse findByUuid(UUID uuid) {
    UserResponse user = toUserResponse(component.findByUuid(uuid));
    log.info("User {} found by Uuid", user);
    return user;
  }

  @Override
  public UserResponse findByEmail(String email) {
    UserResponse user = toUserResponse(component.findByEmail(email));
    log.info("User {} found by Email", user);
    return user;
  }

  @Override
  @Hidden
  public Page<UserResponse> findByIsActive(Integer page, Integer size, String direction,
                                           Boolean isActive) {
    throw new OperationNotAllowedException(NOT_IMPLEMENTED.getCode());
  }

  @Override
  public Page<UserResponse> findAll(Integer page, Integer size, String direction, Locale locale,
                                    Principal principal) {
    String safeDirection = direction == null || direction.isBlank() ? "ASC" : direction;
    Pageable pageable =
        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(safeDirection), "id"));
    return toUserResponse(
        component.findAll(pageable), pageable
    );
  }

  @Override
  public void updateByUuid(UUID uuid, UpdateUserRequest request) {
    component.updateKeycloakUser(uuid, toKeycloakUser(request));
  }

  @Override
  public UserResponse create(Principal principal, CreateUserRequest request) {
    return toUserResponse(
        component.createKeycloakUser(toKeycloakUser(request))
    );
  }

  private UserResponse toUserResponse(KeycloakUser keycloakUser) {
    return UserResponse.builder()
        .uuid(keycloakUser.id())
        .username(keycloakUser.username())
        .firstName(keycloakUser.firstName())
        .lastName(keycloakUser.lastName())
        .email(keycloakUser.email())
        .emailVerified(keycloakUser.emailVerified())
        .enabled(keycloakUser.enabled())
        .createdTimestamp(keycloakUser.createdTimestamp())
        .requiredActions(keycloakUser.requiredActions())
        .build();
  }

  private Page<UserResponse> toUserResponse(Page<KeycloakUser> keycloakUsers, Pageable pageable) {
    if (keycloakUsers == null) {
      return Page.empty();
    } else {
      List<UserResponse> dtoList = keycloakUsers.stream()
          .map(this::toUserResponse)
          .toList();
      return new PageImpl<>(dtoList, pageable, keycloakUsers.getTotalElements());
    }
  }

  private CreateKeycloakUser toKeycloakUser(CreateUserRequest request) {
    return CreateKeycloakUser.builder()
        .name(request.name())
        .surname(request.surname())
        .email(request.email())
        .groupUuid(request.groupUuid())
        .build();
  }

  private UpdateKeycloakUser toKeycloakUser(UpdateUserRequest request) {
    return UpdateKeycloakUser.builder()
        .name(request.name())
        .surname(request.surname())
        .email(request.email())
        .role(request.role())
        .build();
  }
}
