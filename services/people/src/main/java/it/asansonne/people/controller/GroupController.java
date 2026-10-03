package it.asansonne.people.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.common.rest.controller.CrudController;
import java.security.Principal;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()")
@Tag(name = "groups", description = "group.tag.description")
@ApiResponse(responseCode = "401", description = "common.response.401.description", content = @Content)
@ApiResponse(responseCode = "403", description = "common.response.403.description", content = @Content)
public interface GroupController extends CrudController<CreateGroup, UpdateGroup, FilterGroup, Group> {

  @Override
  @Operation(operationId = "groupFindByUuid", summary = "group.find.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "group.find.by.uuid.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "group.find.by.uuid.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  Group findByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "group.uuid.description",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);

  @Override
  @Operation(operationId = "groupFindByIsActive", summary = "group.find.by.is.active.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "group.find.by.is.active.response.200.description", useReturnTypeSchema = true)
  Page<Group> findByIsActive(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "common.is.active.description", example = "true") Boolean isActive,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "groupFindAll", summary = "group.find.all.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "group.find.all.response.200.description", useReturnTypeSchema = true)
  Page<Group> findAll(
      @Parameter(hidden = true) Principal principal, @ParameterObject FilterGroup filter,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "groupCreate", summary = "group.create.summary")
  @ApiResponse(responseCode = "201", description = "group.create.response.201.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "400", description = "common.response.400.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  @ApiResponse(responseCode = "409", description = "group.create.response.409.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  Group create(@Parameter(hidden = true) Principal principal, CreateGroup request);

  @Override
  @Operation(operationId = "groupUpdateByUuid", summary = "group.update.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "group.update.by.uuid.response.200.description", content = @Content)
  void updateByUuid(@Parameter(description = "group.uuid.description",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid, UpdateGroup request);

  @Override
  @Operation(operationId = "groupDeleteByUuid", summary = "group.delete.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "204", description = "group.delete.by.uuid.response.204.description", content = @Content)
  void deleteByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "group.uuid.description",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);
}
