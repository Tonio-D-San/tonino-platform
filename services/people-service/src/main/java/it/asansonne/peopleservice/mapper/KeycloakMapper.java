package it.asansonne.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.exception.custom.DataIntegrityException;
import it.asansonne.peopleservice.model.GroupModel;
import it.asansonne.peopleservice.model.UserModel;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakMapper
{

  public UserModel toUser(KeycloakUser keycloakUser, CreateUser input, GroupModel group) {
    if (keycloakUser == null) {
      throw new DataIntegrityException(BAD_REQUEST.getCode(), "KeycloakUser is null");
    }
    return UserModel.builder()
        .name(keycloakUser.firstName())
        .surname(keycloakUser.lastName())
        .email(keycloakUser.email())
        .phoneNumber(input.phoneNumber())
        .group(group)
        .uuid(keycloakUser.id())
        .isActive(true)
        .build();
  }

  public CreateKeycloakUser toKeycloakUser(CreateUser input, UUID groupUuid) {
    if (input == null) {
      throw new DataIntegrityException(BAD_REQUEST.getCode(), "Input is null");
    }
    return CreateKeycloakUser.builder()
        .name(input.name())
        .surname(input.surname())
        .email(input.email())
        .groupUuid(groupUuid)
        .build();
  }

}
