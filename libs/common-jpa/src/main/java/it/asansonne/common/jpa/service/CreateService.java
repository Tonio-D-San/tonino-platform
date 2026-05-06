package it.asansonne.common.jpa.service;

import it.asansonne.common.jpa.model.BaseModel;
import java.security.Principal;

@SuppressWarnings("unused")
public interface CreateService<M extends BaseModel> {
  M create(Principal principal, M model);

}
