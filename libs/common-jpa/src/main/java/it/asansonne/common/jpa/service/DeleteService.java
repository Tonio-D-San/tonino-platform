package it.asansonne.common.jpa.service;

import it.asansonne.common.jpa.model.BaseModel;
import java.security.Principal;

@SuppressWarnings("unused")
public interface DeleteService<M extends BaseModel> {
  Boolean deleteByUuid(Principal principal, M model);

}
