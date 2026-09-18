package it.asansonne.rest.jpa.peopleservice.csr.repository.specification;

import it.asansonne.common.jpa.repository.specification.ModelSpecifications;
import it.asansonne.common.jpa.util.SpecificationUtils;
import it.asansonne.rest.jpa.peopleservice.dto.request.FilterGroup;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

/**
 * The type Business user specifications.
 */
@Slf4j
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public final class GroupSpecifications implements ModelSpecifications<GroupModel, FilterGroup> {

  /**
   * With filter specification.
   *
   * @param filter the filter
   * @return the specification
   */

  @Override
  public Specification<GroupModel> withFilter(FilterGroup filter) {
    return Specification.allOf(
        SpecificationUtils.hasUuid(filter.uuid()),
        SpecificationUtils.isActive(filter.isActive()),
        roleLike(filter.role()),
        pathLike(filter.path()),
        descriptionLike(filter.description())
    );
  }

  /**
   * Name like specification.
   *
   * @param role the name
   * @return the specification
   */
  public static Specification<GroupModel> roleLike(String role) {
    return SpecificationUtils.likeIgnoringShortValue("role", role);
  }

  /**
   * Surname like specification.
   *
   * @param path the surname
   * @return the specification
   */
  public static Specification<GroupModel> pathLike(String path) {
    return SpecificationUtils.likeIgnoringShortValue("path", path);
  }

  /**
   * Description like specification.
   *
   * @param description the surname
   * @return the specification
   */
  public static Specification<GroupModel> descriptionLike(String description) {
    return SpecificationUtils.likeIgnoringShortValue("description", description);
  }

}