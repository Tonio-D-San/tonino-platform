package it.asansonne.rest.jpa.identityservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.MODEL_NOT_FOUND;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.identity.dto.request.UpdateGroup;
import it.asansonne.common.identity.dto.response.Group;
import it.asansonne.common.rest.mapper.ResponseMapper;
import it.asansonne.common.rest.mapper.UpdateRequestMapper;
import it.asansonne.rest.jpa.identityservice.model.GroupModel;
import it.asansonne.rest.jpa.identityservice.model.UserModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * The type User mapper.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMapper implements
    UpdateRequestMapper<UpdateGroup, GroupModel>, ResponseMapper<GroupModel, Group> {

  @Override
  public GroupModel updateToModel(UpdateGroup input) {
    return GroupModel.builder().description(input.description()).build();
  }

  @Override
  public Group toDto(GroupModel model) {
    if (model == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    log.debug("Mapping Complete Group: {}", model);
    List<UserModel> users = model.getUsers();
    Group out = Group.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .role(model.getRole())
        .path(model.getPath())
        .description(model.getDescription())
        .users(users == null ? List.of() :
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
