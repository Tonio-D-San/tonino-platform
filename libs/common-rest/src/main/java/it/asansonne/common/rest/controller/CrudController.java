package it.asansonne.common.rest.controller;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;
import it.asansonne.common.rest.dto.Response;

@SuppressWarnings("unused")
public interface CrudController<C extends Create, U extends Update, F extends Filter, R extends Response>
    extends GetController<F, R>, PostController<C, R>, PatchController<U>, DeleteController {
}
