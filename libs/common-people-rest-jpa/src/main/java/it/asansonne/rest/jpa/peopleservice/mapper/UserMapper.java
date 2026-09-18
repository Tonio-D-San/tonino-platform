package it.asansonne.rest.jpa.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.DTO_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.MODEL_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.rest.mapper.CreateRequestMapper;
import it.asansonne.common.rest.mapper.ResponseMapper;
import it.asansonne.common.rest.mapper.UpdateRequestMapper;
import it.asansonne.rest.jpa.peopleservice.dto.request.CreateUser;
import it.asansonne.rest.jpa.peopleservice.dto.request.UpdateUser;
import it.asansonne.rest.jpa.peopleservice.dto.response.User;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper
    implements CreateRequestMapper<CreateUser, UserModel>,
    UpdateRequestMapper<UpdateUser, UserModel>,
    ResponseMapper<UserModel, User> {

  @Override
  public UserModel createToModel(CreateUser input) {
    if (input == null) {
      throw new NotFoundException(DTO_NOT_FOUND.getCode());
    }
    log.debug(
        "Mapping UserInput: {}"
            + "\n remember to mapping Organization and Group in the right layer",
        input
    );
    UserModel user = UserModel.builder()
        .name(input.name())
        .surname(input.surname())
        .email(input.email())
        .phoneNumber(input.phoneNumber())
        .build();
    log.debug("to User: {}", user);
    return user;
  }

  @Override
  public UserModel updateToModel(UpdateUser input) {
    if (input == null) {
      throw new NotFoundException(DTO_NOT_FOUND.getCode());
    }
    log.debug("Mapping UpdateUserInput: {}", input);
    UserModel user = UserModel.builder()
        .email(input.email())
        .phoneNumber(input.phoneNumber())
        .build();
    log.debug("to UserModel: {}", user);
    return user;
  }

  @Override
  public User toDto(UserModel model) {
    if (model == null) {
      throw new NotFoundException(DTO_NOT_FOUND.getCode());
    }
    log.debug("Mapping Complete User: {}", model);
    User out = User.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .isActive(model.getIsActive())
        .name(model.getName())
        .surname(model.getSurname())
        .email(model.getEmail())
        .phoneNumber(model.getPhoneNumber())
        .group(new GroupMapper().toLittleDto(model.getGroup()))
        .build();
    log.debug("to Complete UserOutput: {}", out.toString());
    return out;
  }

  @Override
  public User toLittleDto(UserModel model) {
    if (model == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    log.debug("Mapping User: {}", model);
    User out = User.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .isActive(model.getIsActive())
        .name(model.getName())
        .surname(model.getSurname())
        .email(model.getEmail())
        .phoneNumber(model.getPhoneNumber())
        .group(null)
        .build();
    log.debug("to slim UserOutput: {}", out);
    return out;
  }

}
