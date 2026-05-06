package it.asansonne.common.jpa.model;

import it.asansonne.common.core.model.Models;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class BaseModel implements Models {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "uuid", nullable = false, unique = true, columnDefinition = "UUID")
  @Builder.Default
  private UUID uuid = UUID.randomUUID();

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected Long createdAt = System.currentTimeMillis();

  @Column(name = "updated_at", nullable = false)
  @Setter
  @Builder.Default
  protected Long updatedAt = System.currentTimeMillis();

  @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
  @Builder.Default
  protected Boolean isActive = true;

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = System.currentTimeMillis();
  }
}
