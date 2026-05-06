package it.asansonne.peopleservice.dto.input;

import it.asansonne.common.graphql.dto.input.Update;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record UpdateGroup(
    String description
) implements Update {
}
