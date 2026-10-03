package it.asansonne.common.keycloak.dto.input;

import it.asansonne.common.core.dto.Dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record UpdateKcGroup(
    @NotBlank
    @Size(max = 255, message = "name troppo lungo")
    String name,

    Map<String, List<String>> attributes
) implements Dto {
}
