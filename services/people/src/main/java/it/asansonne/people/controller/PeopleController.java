package it.asansonne.people.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.people.dto.request.CreateUser;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.common.people.dto.request.UpdateUser;
import it.asansonne.common.people.dto.response.User;
import it.asansonne.common.rest.controller.CrudController;
import java.security.Principal;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()")
@Tag(name = "users", description = "user.tag.description")
@ApiResponse(responseCode = "401", description = "common.response.401.description", content = @Content)
@ApiResponse(responseCode = "403", description = "common.response.403.description", content = @Content)
public interface PeopleController extends CrudController<CreateUser, UpdateUser, FilterUser, User> {

  @Override
  @Operation(operationId = "peopleFindByUuid", summary = "user.find.by.uuid.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.uuid.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.find.by.uuid.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);

  @Override
  @Operation(operationId = "peopleFindByIsActive", summary = "user.find.by.is.active.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.is.active.response.200.description", useReturnTypeSchema = true)
  Page<User> findByIsActive(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "common.is.active.description", example = "true") Boolean isActive,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "peopleFindAll", summary = "user.find.all.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "user.find.all.response.200.description", useReturnTypeSchema = true)
  Page<User> findAll(
      @Parameter(hidden = true) Principal principal, @ParameterObject FilterUser filter,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "peopleCreate", summary = "user.create.summary")
  @ApiResponse(responseCode = "201", description = "user.create.response.201.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "400", description = "common.response.400.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  @ApiResponse(responseCode = "409", description = "user.create.response.409.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User create(@Parameter(hidden = true) Principal principal, CreateUser request);

  @Override
  @Operation(operationId = "peopleUpdateByUuid", summary = "user.update.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "user.update.by.uuid.response.200.description", content = @Content)
  void updateByUuid(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description", example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid,
      UpdateUser request);

  @Override
  @Operation(operationId = "peopleDeleteByUuid", summary = "user.delete.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "204", description = "user.delete.by.uuid.response.204.description", content = @Content)
  void deleteByUuid(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description", example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);
}
