package it.asansonne.rest.jpa.peopleservice.csr.service;

import it.asansonne.common.jpa.service.CrudService;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import java.security.Principal;

/**
 * The interface Business user component.
 */
public interface GroupService extends CrudService<GroupModel, FilterGroup> {
  /**
   * Group by name group.
   *
   * @param name the name
   * @return the group
   */
  GroupModel findByRole(Principal principal, String name);

  /**
   * Find by path optional.
   *
   * @param path the path
   * @return the optional
   */
  GroupModel findByPath(Principal principal, String path);

  /**
   * Find by description optional.
   *
   * @param description the description
   * @return the optional
   */
  GroupModel findByDescription(Principal principal, String description);

}
