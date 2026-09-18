package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Filter;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Group filter input.
 */
@Builder
public record FilterGroup(
    UUID uuid,
    Boolean isActive,
    String role,
    String path,
    String description
) implements Filter {
}
