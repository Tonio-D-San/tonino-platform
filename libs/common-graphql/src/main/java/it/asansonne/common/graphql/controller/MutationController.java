package it.asansonne.common.graphql.controller;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Update;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;

@SuppressWarnings("unused")
public interface MutationController<O extends Dto, I extends Create, U extends Update> {

  O create(Principal principal, @Valid @Argument I input);

  O updateByUuid(Principal principal, @Argument UUID uuid, @Valid @Argument U input);

  Boolean deleteByUuid(Principal principal, @Argument UUID uuid, @Argument Boolean delete);
}
