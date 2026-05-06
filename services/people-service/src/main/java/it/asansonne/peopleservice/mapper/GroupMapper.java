package it.asansonne.peopleservice.mapper;

import static it.asansonne.common.core.enums.ErrorMessage.DTO_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.MODEL_NOT_FOUND;
import static it.asansonne.common.core.enums.ErrorMessage.NOT_IMPLEMENTED;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.mapper.InputMapper;
import it.asansonne.common.graphql.mapper.OutputMapper;
import it.asansonne.peopleservice.dto.input.CreateGroup;
import it.asansonne.peopleservice.dto.input.UpdateGroup;
import it.asansonne.peopleservice.dto.output.Group;
import it.asansonne.peopleservice.model.GroupModel;
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
public class GroupMapper
    implements InputMapper<CreateGroup, UpdateGroup, GroupModel>,
    OutputMapper<GroupModel, Group, OutputPage<Group>> {

  @Override
  public GroupModel toModel(CreateGroup input) {
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
  public GroupModel toModel(UpdateGroup input) {
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
    log.debug("Mapping Group: {}", model);
    Group out = Group.builder()
        .uuid(model.getUuid())
        .createdAt(model.getCreatedAt())
        .updatedAt(model.getUpdatedAt())
        .role(model.getRole())
        .path(model.getPath())
        .description(model.getDescription())
        .build();
    log.debug("to slim GroupOutput: {}", out.toString());
    return out;
  }

  @Override
  public OutputPage<Group> toPage(Page<GroupModel> models) {
    if (models == null) {
      throw new NotFoundException(MODEL_NOT_FOUND.getCode());
    }
    Page<Group> dtoPage = this.toDto(models);
    return new OutputPage<>(
        dtoPage.getContent(),
        dtoPage.getNumber(),
        dtoPage.getSize(),
        dtoPage.getTotalElements(),
        dtoPage.getTotalPages()
    );
  }

}
