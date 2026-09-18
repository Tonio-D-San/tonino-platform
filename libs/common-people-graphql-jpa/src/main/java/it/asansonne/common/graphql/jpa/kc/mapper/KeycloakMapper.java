package it.asansonne.common.graphql.jpa.kc.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.common.core.exception.custom.DataIntegrityException;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;
import it.asansonne.common.graphql.jpa.kc.model.UserModel;
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

  public UserModel toUser(KcUser kcUser, CreateUser input, GroupModel group) {
    if (kcUser == null) {
      throw new DataIntegrityException(BAD_REQUEST.getCode(), "KcUser is null");
    }
    return UserModel.builder()
        .name(kcUser.firstName())
        .surname(kcUser.lastName())
        .email(kcUser.email())
        .phoneNumber(input.phoneNumber())
        .group(group)
        .uuid(kcUser.id())
        .isActive(true)
        .build();
  }

  public CreateKcUser toKeycloakUser(CreateUser input, UUID groupUuid) {
    if (input == null) {
      throw new DataIntegrityException(BAD_REQUEST.getCode(), "Input is null");
    }
    return CreateKcUser.builder()
        .name(input.name())
        .surname(input.surname())
        .email(input.email())
        .groupUuid(groupUuid)
        .build();
  }

}
