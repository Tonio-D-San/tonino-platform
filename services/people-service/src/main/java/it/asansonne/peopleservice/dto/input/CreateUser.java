package it.asansonne.peopleservice.dto.input;

import it.asansonne.common.graphql.dto.input.Create;
import it.asansonne.peopleservice.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateUser(
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
    @Pattern(
        regexp = "^\\+?\\d{6,13}$",
        message = "phoneNumber non valido"
    )
    String phoneNumber,

    @NotNull
    UUID organizationUuid,

    @NotNull
    UserRole role
) implements Create {
}
