package it.asansonne.common.graphql.jpa.kc.dto.input;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.graphql.dto.input.Input;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Organization filter input.
 */
@Builder
public record FilterGroup(
    UUID uuid,
    Boolean isActive,
    String role,
    String path,
    String description
) implements Filter, Input {
}
