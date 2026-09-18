package it.asansonne.common.graphql.jpa.kc.dto.input;

import it.asansonne.common.core.dto.Update;
import it.asansonne.common.graphql.dto.input.Input;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record UpdateGroup(
    String description
) implements Update, Input {
}
