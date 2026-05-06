package it.asansonne.common.jpa.service;

import it.asansonne.common.jpa.model.BaseModel;
import java.security.Principal;

@SuppressWarnings("unused")
public interface UpdateService<M extends BaseModel> {
  M update(Principal principal, M model);

}
