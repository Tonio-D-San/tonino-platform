package it.asansonne.common.rest.controller;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;

@SuppressWarnings("unused")
public interface CrudController<C extends Create, U extends Update, F extends Filter, R extends Dto>
    extends GetController<F, R>, PostController<C, R>, PatchController<U>, DeleteController {
}
