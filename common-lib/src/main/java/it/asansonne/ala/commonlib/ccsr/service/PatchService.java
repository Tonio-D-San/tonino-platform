package it.asansonne.ala.commonlib.ccsr.service;

import it.asansonne.ala.commonlib.ccsr.model.BaseModel;

public interface PatchService<M extends BaseModel> {

  void update(M model);

}
