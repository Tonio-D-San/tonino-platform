package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Create;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateUser(
    @NotBlank(message = "error.people.user.name.required")
    @Size(max = 255, message = "error.people.user.name.size")
    String name,

    @NotBlank(message = "error.people.user.surname.required")
    @Size(max = 255, message = "error.people.user.surname.size")
    String surname,

    @NotBlank(message = "error.people.user.email.required")
    @Email(message = "error.people.user.email.invalid")
    String email,

    String pswTemp,

    @NotBlank(message = "error.people.user.phone.required")
    @Pattern(
        regexp = "^\\+?\\d{6,13}$",
        message = "error.people.user.phone.invalid"
    )
    String phoneNumber,

    @NotNull(message = "error.people.user.role.required")
    String role
) implements Create {
}
