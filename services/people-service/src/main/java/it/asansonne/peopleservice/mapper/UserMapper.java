package it.asansonne.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.DTO_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.MODEL_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.mapper.InputMapper;
import it.asansonne.common.graphql.mapper.OutputMapper;
import it.asansonne.peopleservice.dto.input.CreateUser;
import it.asansonne.peopleservice.dto.input.UpdateUser;
import it.asansonne.peopleservice.dto.output.User;
import it.asansonne.peopleservice.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper
    implements InputMapper<CreateUser, UpdateUser, UserModel>,
    OutputMapper<UserModel, User, OutputPage<User>> {

  @Override
  public UserModel toModel(CreateUser input) {
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
  public UserModel toModel(UpdateUser input) {
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

  @Override
  public OutputPage<User> toPage(Page<UserModel> models) {
    if (models == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    Page<User> dtoPage = this.toDto(models);
    return new OutputPage<>(
        dtoPage.getContent(),
        dtoPage.getNumber(),
        dtoPage.getSize(),
        dtoPage.getTotalElements(),
        dtoPage.getTotalPages()
    );
  }

}
