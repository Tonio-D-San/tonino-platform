package it.asansonne.common.jpa.service;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.jpa.model.BaseModel;

@SuppressWarnings("unused")
public interface CrudService<M extends BaseModel, F extends Filter>
    extends FindService<M, F>, CreateService<M>, UpdateService<M>, DeleteService<M> {
}
