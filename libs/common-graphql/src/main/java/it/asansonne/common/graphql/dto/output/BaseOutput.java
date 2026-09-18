package it.asansonne.common.graphql.dto.output;

import java.util.UUID;

@SuppressWarnings("unused")
public interface BaseOutput extends Output {
  UUID uuid();

  Long createdAt();

  Long updatedAt();

  Boolean isActive();
}
