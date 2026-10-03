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
@Tag(name = "Utenti")
@ApiResponse(responseCode = "401", description = "Autenticazione richiesta", content = @Content)
@ApiResponse(responseCode = "403", description = "Accesso non consentito", content = @Content)
public interface PeopleController extends CrudController<CreateUser, UpdateUser, FilterUser, User> {

  @Override
  @Operation(operationId = "peopleFindByUuid", summary = "Cerca un utente tramite UUID")
  @ApiResponse(responseCode = "200", description = "Utente trovato", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "Utente non trovato",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User findByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "UUID dell'utente",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);

  @Override
  @Operation(operationId = "peopleFindByIsActive", summary = "Elenca gli utenti per stato di attivazione")
  @ApiResponse(responseCode = "200", description = "Pagina dgli utenti", useReturnTypeSchema = true)
  Page<User> findByIsActive(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "Stato di attivazione da cercare", example = "true") Boolean isActive,
      @Parameter(description = "Indice della pagina, a partire da zero", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "Numero di elementi per pagina", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "Ordinamento per updatedAt: ASC o DESC", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "peopleFindAll", summary = "Cerca gli utenti con filtri (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Pagina dgli utenti", useReturnTypeSchema = true)
  Page<User> findAll(
      @Parameter(hidden = true) Principal principal, @ParameterObject FilterUser filter,
      @Parameter(description = "Indice della pagina, a partire da zero", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "Numero di elementi per pagina", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "Ordinamento per updatedAt: ASC o DESC", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "peopleCreate", summary = "Crea un utente")
  @ApiResponse(responseCode = "201", description = "Utente creato", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  @ApiResponse(responseCode = "409", description = "Utente duplicato o conflitto con i vincoli dei dati",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  User create(@Parameter(hidden = true) Principal principal, CreateUser request);

  @Override
  @Operation(operationId = "peopleUpdateByUuid", summary = "Aggiorna un utente (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Operazione completata", content = @Content)
  void updateByUuid(@Parameter(description = "UUID dell'utente",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid, UpdateUser request);

  @Override
  @Operation(operationId = "peopleDeleteByUuid", summary = "Elimina un utente (implementazione non completata)")
  @ApiResponse(responseCode = "204", description = "Operazione completata senza contenuto", content = @Content)
  void deleteByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "UUID dell'utente",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);
}
