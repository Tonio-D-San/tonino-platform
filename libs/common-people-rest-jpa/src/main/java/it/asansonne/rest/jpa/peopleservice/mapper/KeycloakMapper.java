package it.asansonne.rest.jpa.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import it.asansonne.rest.jpa.peopleservice.dto.request.CreateUser;
import it.asansonne.rest.jpa.peopleservice.dto.request.UpdateUser;
import it.asansonne.rest.jpa.peopleservice.exception.custom.DataIntegrityException;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
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
public class KeycloakMapper {

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

  public CreateKcUser toCreateKcUser(CreateUser input, UUID groupUuid) {
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

  public UpdateKcUser toUpdateKcUser(UpdateUser input) {
    if (input == null) {
      throw new DataIntegrityException(BAD_REQUEST.getCode(), "Input is null");
    }
    return UpdateKcUser.builder().email(input.email()).build();
  }

}
