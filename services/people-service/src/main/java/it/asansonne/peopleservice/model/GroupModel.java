package it.asansonne.peopleservice.model;

import it.asansonne.common.jpa.model.BaseModel;
import it.asansonne.peopleservice.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The type Group model.
 */
@Entity
@Table(name = "groups")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupModel extends BaseModel {
  @Column(nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(nullable = false)
  private String path;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  private List<UserModel> users;
}
