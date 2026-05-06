package it.asansonne.common.rest.component;

import java.util.UUID;

@SuppressWarnings("unused")
public interface DeleteComponent {
  void deleteByUuid(UUID uuid);
}
