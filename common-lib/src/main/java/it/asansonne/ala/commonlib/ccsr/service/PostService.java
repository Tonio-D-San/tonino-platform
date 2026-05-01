package it.asansonne.ala.commonlib.ccsr.service;

import it.asansonne.ala.commonlib.ccsr.model.BaseModel;

public interface PostService<M extends BaseModel> {

  M create(M model);
}
