package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Filter;
import lombok.Builder;

/**
 * The type Group filter input.
 */
@Builder
public record FilterGroup(
    String uuid,
    Boolean isActive,
    String role,
    String path,
    String description
) implements Filter {
}
