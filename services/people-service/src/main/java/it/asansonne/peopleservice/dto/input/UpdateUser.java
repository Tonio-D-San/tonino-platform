package it.asansonne.peopleservice.dto.input;

import it.asansonne.common.graphql.dto.input.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * The type Create Business User.
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
