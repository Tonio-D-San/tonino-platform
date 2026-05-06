package it.asansonne.keycloakservice.dto.request;

import it.asansonne.common.rest.dto.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateUserRequest(
    @NotBlank
    @Size(max = 255, message = "name troppo lungo")
    String name,

    @NotBlank
    @Size(max = 255, message = "surname troppo lungo")
    String surname,

    @NotBlank
    @Email(message = "email non valida")
    String email,

    @NotNull
    UUID groupUuid
) implements Request {
}
