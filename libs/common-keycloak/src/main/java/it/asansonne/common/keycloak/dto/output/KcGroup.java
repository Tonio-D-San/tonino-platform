package it.asansonne.common.keycloak.dto.output;

import it.asansonne.common.core.dto.Dto;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record KcGroup(
    UUID id,
    String name,
    String path,
    Map<String, List<String>> attributes,
    List<KcGroup> subGroups
) implements Dto {

  public static final String DESCRIPTION = "description";

  public String description() {
    if (attributes == null || attributes.get(DESCRIPTION) == null
        || attributes.get(DESCRIPTION).isEmpty()) {
      return null;
    }
    return attributes.get(DESCRIPTION).getFirst();
  }
}
