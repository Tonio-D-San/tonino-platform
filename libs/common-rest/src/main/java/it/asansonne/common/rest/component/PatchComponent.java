package it.asansonne.common.rest.component;

import it.asansonne.common.rest.dto.Request;
import java.util.UUID;

@SuppressWarnings("unused")
public interface PatchComponent<R extends Request> {
  void updateByUuid(UUID uuid, R request);
}
