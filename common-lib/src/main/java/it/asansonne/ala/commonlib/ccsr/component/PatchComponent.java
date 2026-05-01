package it.asansonne.ala.commonlib.ccsr.component;

import it.asansonne.ala.commonlib.dto.Request;
import java.util.UUID;

public interface PatchComponent<R extends Request> {

  void updateByUuid(UUID uuid, R request);
}
