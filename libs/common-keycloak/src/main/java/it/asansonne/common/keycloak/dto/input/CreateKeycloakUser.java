package it.asansonne.common.keycloak.dto.input;

import it.asansonne.common.core.dto.Dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateKeycloakUser(
    @NotBlank
    @Size(max = 255, message = "name troppo lungo")
    String name,

    @NotBlank
    @Size(max = 255, message = "surname troppo lungo")
    String surname,

    @NotBlank
    @Email(message = "email non valida")
    String email,

    @NotBlank
    String passwordTemp,

    @NotNull
    UUID groupUuid
) implements Dto {
}
