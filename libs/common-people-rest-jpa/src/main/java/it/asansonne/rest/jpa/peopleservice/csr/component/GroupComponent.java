package it.asansonne.rest.jpa.peopleservice.csr.component;

import it.asansonne.common.rest.component.CrudComponent;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import java.security.Principal;

/**
 * The interface Business user component.
 */
public interface GroupComponent extends CrudComponent<CreateGroup, UpdateGroup, FilterGroup, Group> {

  /**
   * Find by role group.
   *
   * @param principal the principal
   * @param name      the name
   * @return the group
   */
  Group findByRole(Principal principal, String name);

  /**
   * Find by path group.
   *
   * @param principal the principal
   * @param path      the path
   * @return the group
   */
  Group findByPath(Principal principal, String path);

  /**
   * Find by description group.
   *
   * @param principal   the principal
   * @param description the description
   * @return the group
   */
  Group findByDescription(Principal principal, String description);
}
