package it.asansonne.common.graphql.jpa.kc.csr.service;

import it.asansonne.common.jpa.service.CrudService;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.graphql.jpa.kc.model.GroupModel;

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
  GroupModel findByRole(String name);

}
