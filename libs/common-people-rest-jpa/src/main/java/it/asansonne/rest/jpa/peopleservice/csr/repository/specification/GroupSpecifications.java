package it.asansonne.rest.jpa.peopleservice.csr.repository.specification;

import static it.asansonne.common.core.enums.ErrorMessage.FILTER_ERROR;

import it.asansonne.common.core.exception.custom.BadRequestException;
import it.asansonne.common.jpa.repository.specification.ModelSpecifications;
import it.asansonne.common.jpa.util.SpecificationUtils;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.rest.jpa.peopleservice.model.GroupModel;
import java.util.Locale;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * The type Business user specifications.
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class GroupSpecifications implements ModelSpecifications<GroupModel, FilterGroup> {

  /**
   * With filter specification.
   *
   * @param filter the filter
   * @return the specification
   */

  @Override
  public Specification<GroupModel> withFilter(FilterGroup filter) {
    return Specification.allOf(
        uuidLike(filter.uuid()),
        SpecificationUtils.isActive(filter.isActive()),
        roleLike(filter.role()),
        pathLike(filter.path()),
        descriptionLike(filter.description())
    );
  }

  public static Specification<GroupModel> uuidLike(String uuid) {
    return (root, _, cb) -> {
      if (!SpecificationUtils.hasText(uuid)) {
        return null;
      }
      String normalized = SpecificationUtils.normalizeOrNull("uuid", uuid);
      if (normalized == null) {
        throw new BadRequestException(FILTER_ERROR.getCode(), SpecificationUtils.MIN_SEARCH_LENGTH);
      }
      return cb.like(
          cb.lower(root.<UUID>get("uuid").cast(String.class)),
          "%" + normalized.toLowerCase(Locale.ROOT) + "%"
      );
    };
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
