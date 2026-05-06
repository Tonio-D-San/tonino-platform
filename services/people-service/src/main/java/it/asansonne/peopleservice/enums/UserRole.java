package it.asansonne.peopleservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Business user role.
 */
@Getter
@AllArgsConstructor
public enum UserRole implements RoleEnums {
  /**
   * Superuser role.
   */
  SUPER_USER("superadmin"),
  /**
   * Admin business user role.
   */
  ADMIN("admin"),
  /**
   * Operator business user role.
   */
  OPERATOR("operator");

  private final String role;
}