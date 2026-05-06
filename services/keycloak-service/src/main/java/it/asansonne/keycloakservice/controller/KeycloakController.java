package it.asansonne.keycloakservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.rest.controller.DeleteController;
import it.asansonne.common.rest.controller.GetController;
import it.asansonne.common.rest.controller.PatchController;
import it.asansonne.common.rest.controller.PostController;
import it.asansonne.keycloakservice.dto.request.CreateUserRequest;
import it.asansonne.keycloakservice.dto.request.UpdateUserRequest;
import it.asansonne.keycloakservice.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "UserController v1")
public interface KeycloakController
    extends GetController<UserResponse>, PostController<CreateUserRequest, UserResponse>,
    DeleteController, PatchController<UpdateUserRequest> {

  @Operation(summary = "resource.find.by.email")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "resource.find.201.description"
      ), @ApiResponse(
      responseCode = "401",
      description = "resource.401.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "401 - UNAUTHORIZED",
                  value = """
                      {
                      "status": "UNAUTHORIZED",
                      "message": \
                      "Unauthorized message"
                       }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  ), @ApiResponse(
      responseCode = "403",
      description = "resource.403.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "403 - FORBIDDEN",
                  value = """
                      {
                      "status": "FORBIDDEN",
                      "message": \
                      "Forbidden message"
                      }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  ), @ApiResponse(
      responseCode = "404",
      description = "resource.404.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "404 - NOT FOUND",
                  value = """
                      {
                      "status": "NOT_FOUND",
                      "message": \
                      "Not found message"
                      , "validations": \
                      null }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  )
  })
  @GetMapping(value = "/my/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  UserResponse findByEmail(@PathVariable String email);
}
