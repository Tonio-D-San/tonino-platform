package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Update;
import lombok.Builder;

/**
 * The type Update Group.
 */
@Builder
public record UpdateGroup(
    String description
) implements Update {
}
