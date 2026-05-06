package it.asansonne.common.graphql.component;

import java.util.UUID;

@SuppressWarnings("unused")
public interface DeleteComponent {
  Boolean deleteByUuid(UUID uuid);
}
