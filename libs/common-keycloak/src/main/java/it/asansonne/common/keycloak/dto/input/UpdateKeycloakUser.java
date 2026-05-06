package it.asansonne.common.keycloak.dto.input;

import it.asansonne.common.core.dto.Dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record UpdateKeycloakUser(
    @Size(max = 255, message = "name troppo lungo")
    String name,

    @Size(max = 255, message = "surname troppo lungo")
    String surname,

    @Email(message = "email non valida")
    String email,

    UserRole role
) implements Dto {
}
