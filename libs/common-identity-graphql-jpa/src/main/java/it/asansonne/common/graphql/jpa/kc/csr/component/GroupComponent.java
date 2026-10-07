package it.asansonne.common.graphql.jpa.kc.csr.component;

import it.asansonne.common.graphql.component.CrudComponent;
import it.asansonne.common.identity.dto.request.CreateGroup;
import it.asansonne.common.identity.dto.request.FilterGroup;
import it.asansonne.common.identity.dto.request.UpdateGroup;
import it.asansonne.common.identity.dto.response.Group;

/**
 * The interface Business user component.
 */
public interface GroupComponent
    extends CrudComponent<Group, CreateGroup, FilterGroup, UpdateGroup> {
  Group findByRole(String name);
}
