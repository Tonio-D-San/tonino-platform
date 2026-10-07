package it.asansonne.common.identity.dto.request;

import it.asansonne.common.core.dto.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * The type Update Business User.
 */
@Builder
public record UpdateUser(
    @Email(message = "error.identity.user.email.invalid")
    String email,

    @Pattern(
        regexp = "^\\+?\\d{6,13}$",
        message = "error.identity.user.phone.invalid"
    )
    String phoneNumber
) implements Update {
}
