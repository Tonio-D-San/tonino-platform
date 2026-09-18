package it.asansonne.common.graphql.component;

import java.security.Principal;
import java.util.UUID;

@SuppressWarnings("unused")
public interface DeleteComponent {
  Boolean deleteByUuid(Principal principal, UUID uuid);
}
