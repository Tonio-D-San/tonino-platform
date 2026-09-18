package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Filter;
import jakarta.validation.constraints.Email;
import java.util.UUID;
import lombok.Builder;

/**
 * The type User filter input.
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
    String role
) implements Filter {
}
