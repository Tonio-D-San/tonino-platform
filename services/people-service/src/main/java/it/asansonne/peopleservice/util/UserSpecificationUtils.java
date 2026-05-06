package it.asansonne.peopleservice.util;

import static it.asansonne.common.jpa.util.SpecificationUtils.hasText;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class UserSpecificationUtils {

  public static <T> Specification<T> hasEmail(String email) {
    return (root, _, cb) ->
        hasText(email)
            ? cb.equal(cb.lower(root.get("email")), email.trim().toLowerCase())
            : null;
  }

  public static <T> Specification<T> hasPhoneNumber(String phoneNumber) {
    return (root, _, cb) ->
        hasText(phoneNumber)
            ? cb.equal(root.get("phoneNumber"), phoneNumber.trim())
            : null;
  }
}
