package it.asansonne.common.graphql.dto.output;

import java.util.UUID;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Organization.
 */
@Slf4j
@Builder
public record BaseOutput(
    UUID uuid,
    Long createdAt,
    Long updatedAt,
    Boolean isActive
) implements Output {
}
