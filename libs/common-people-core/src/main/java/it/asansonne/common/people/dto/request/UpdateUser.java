package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * The type Update Business User.
 */
@Builder
public record UpdateUser(
    @Email(message = "email non valida")
    String email,

    @Pattern(
        regexp = "^\\+?\\d{6,13}$",
        message = "phoneNumber non valido"
    )
    String phoneNumber
) implements Update {
}
