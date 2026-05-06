package it.asansonne.peopleservice.dto.input;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.peopleservice.enums.UserRole;
import jakarta.validation.constraints.Email;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Organization filter input.
 */
@Builder
public record FilterUser(
    UUID uuid,
    String name,
    String surname,
    Boolean isActive,
    @Email
    String email,
    String phoneNumber,
    UserRole role
) implements Filter {
}
