package it.asansonne.rest.jpa.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.DTO_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.MODEL_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.NOT_IMPLEMENTED;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.common.rest.mapper.CreateRequestMapper;
import it.asansonne.common.rest.mapper.ResponseMapper;
import it.asansonne.common.rest.mapper.UpdateRequestMapper;
import it.asansonne.rest.jpa.peopleservice.dto.request.CreateGroup;
import it.asansonne.rest.jpa.peopleservice.dto.request.UpdateGroup;
import it.asansonne.rest.jpa.peopleservice.dto.response.Group;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMapper implements CreateRequestMapper<CreateGroup, GroupModel>,
    UpdateRequestMapper<UpdateGroup, GroupModel>, ResponseMapper<GroupModel, Group> {

  @Override
  public GroupModel createToModel(CreateGroup input) {
    if (input == null) {
      throw new NotFoundException(DTO_NOT_FOUND.getCode());
    }
    log.debug(
        "Mapping GroupInput: {}"
            + "\n remember to mapping Organization and Group in the right layer",
        input
    );
    GroupModel group = GroupModel.builder()
        .role(input.role())
        .path(input.path())
        .description(input.description())
        .build();
    log.debug("to Group: {}", group.toString());
    return group;
  }

  @Override
  public GroupModel updateToModel(UpdateGroup input) {
    throw new OperationNotAllowedException(NOT_IMPLEMENTED.getCode());
  }

  @Override
  public Group toDto(GroupModel model) {
    if (model == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    log.debug("Mapping Complete Group: {}", model);
    Group out = Group.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .role(model.getRole())
        .path(model.getPath())
        .description(model.getDescription())
        .users(
            model.getUsers().stream()
                .map(bu -> new UserMapper().toLittleDto(bu))
                .toList())
        .build();
    log.debug("to Complete GroupOutput: {}", out.toString());
    return out;
  }

  @Override
  public Group toLittleDto(GroupModel model) {
    if (model == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    log.debug("Mapping Child Group: {}", model);
    Group out = Group.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .role(model.getRole())
        .path(model.getPath())
        .description(model.getDescription())
        .users(null)
        .build();
    log.debug("to little GroupOutput: {}", out.toString());
    return out;
  }

}
