package it.asansonne.rest.jpa.peopleservice.csr.repository.specification;

import it.asansonne.common.jpa.repository.specification.ModelSpecifications;
import it.asansonne.common.jpa.util.SpecificationUtils;
import it.asansonne.common.people.dto.request.FilterUser;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import it.asansonne.rest.jpa.peopleservice.util.UserSpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * The type Business user specifications.
 */
@Slf4j
@Component
public class UserSpecifications implements ModelSpecifications<UserModel, FilterUser> {

  /**
   * With filter specification.
   *
   * @param filter the filter
   * @return the specification
   */

  @Override
  public Specification<UserModel> withFilter(FilterUser filter) {
    return Specification.allOf(
        SpecificationUtils.hasUuid(filter.uuid()),
        SpecificationUtils.isActive(filter.isActive()),
        nameLike(filter.name()),
        surnameLike(filter.surname()),
        UserSpecificationUtils.hasEmail(filter.email()),
        UserSpecificationUtils.hasPhoneNumber(filter.phoneNumber()),
        hasRole(filter.role())
    );
  }

  /**
   * Has group uuid specification.
   *
   * @param role the role
   * @return the specification
   */
  public static Specification<UserModel> hasRole(String role) {
    return (root, _, cb) ->
        role == null
            ? null
            : cb.equal(root.join("group").get("role"), role);
  }

  /**
   * Name like specification.
   *
   * @param name the name
   * @return the specification
   */
  public static Specification<UserModel> nameLike(String name) {
    return SpecificationUtils.likeIgnoringShortValue("name", name);
  }

  /**
   * Surname like specification.
   *
   * @param surname the surname
   * @return the specification
   */
  public static Specification<UserModel> surnameLike(String surname) {
    return SpecificationUtils.likeIgnoringShortValue("surname", surname);
  }

}
