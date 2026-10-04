package it.asansonne.identity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.identity.dto.request.CreateUser;
import it.asansonne.common.identity.dto.request.FilterUser;
import it.asansonne.common.identity.dto.request.UpdateUser;
import it.asansonne.common.identity.dto.response.User;
import it.asansonne.common.rest.controller.CrudController;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@PreAuthorize("isAuthenticated()")
@Tag(name = "users", description = "user.tag.description")
@ApiResponse(responseCode = "401", description = "common.response.401.description", content = @Content)
@ApiResponse(responseCode = "403", description = "common.response.403.description", content = @Content)
public interface IdentityController extends CrudController<CreateUser, UpdateUser, FilterUser, User> {

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(operationId = "identityMe", summary = "user.me.summary")
  @ApiResponse(responseCode = "200", description = "user.me.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.me.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User me(@Parameter(hidden = true) Principal principal);

  @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(operationId = "identityFindByName", summary = "user.find.by.name.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.name.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.find.by.name.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findByName(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.name.description", example = "Mario") @PathVariable String name);

  @GetMapping(value = "/surname/{surname}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(operationId = "identityFindBySurname", summary = "user.find.by.surname.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.surname.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.find.by.surname.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findBySurname(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.surname.description", example = "Rossi") @PathVariable String surname);

  @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Operation(operationId = "identityFindByEmail", summary = "user.find.by.email.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.email.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.find.by.email.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findByEmail(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.email.description", example = "mario.rossi@example.com") @PathVariable String email);

  @Override
  @Operation(operationId = "identityFindByUuid", summary = "user.find.by.uuid.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.uuid.response.200.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "user.find.by.uuid.response.404.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);

  @Override
  @Operation(operationId = "identityFindByIsActive", summary = "user.find.by.is.active.summary")
  @ApiResponse(responseCode = "200", description = "user.find.by.is.active.response.200.description", useReturnTypeSchema = true)
  Page<User> findByIsActive(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "common.is.active.description", example = "true") Boolean isActive,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "identityFindAll", summary = "user.find.all.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "user.find.all.response.200.description", useReturnTypeSchema = true)
  Page<User> findAll(
      @Parameter(hidden = true) Principal principal, @ParameterObject FilterUser filter,
      @Parameter(description = "common.page.description", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "common.size.description", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "common.sort.direction.description", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "identityCreate", summary = "user.create.summary")
  @ApiResponse(responseCode = "201", description = "user.create.response.201.description", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "400", description = "common.response.400.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  @ApiResponse(responseCode = "409", description = "user.create.response.409.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User create(@Parameter(hidden = true) Principal principal, @Valid @RequestBody CreateUser request);

  @Override
  @Operation(operationId = "identityUpdateByUuid", summary = "user.update.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "200", description = "user.update.by.uuid.response.200.description", content = @Content)
  void updateByUuid(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description", example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid,
      @Valid @RequestBody UpdateUser request);

  @Override
  @Operation(operationId = "identityDeleteByUuid", summary = "user.delete.by.uuid.summary", description = "common.operation.not.implemented.description")
  @ApiResponse(responseCode = "204", description = "user.delete.by.uuid.response.204.description", content = @Content)
  void deleteByUuid(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "user.uuid.description", example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);
}
