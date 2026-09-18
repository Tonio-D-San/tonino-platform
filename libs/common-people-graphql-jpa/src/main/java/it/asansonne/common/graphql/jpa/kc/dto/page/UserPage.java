package it.asansonne.common.graphql.jpa.kc.dto.page;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.jpa.kc.dto.output.User;
import java.util.List;
import lombok.Builder;

/**
 * The type Business user page.
 */
@Builder
public record UserPage(
    List<User> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) implements OutputPage<User> {
}