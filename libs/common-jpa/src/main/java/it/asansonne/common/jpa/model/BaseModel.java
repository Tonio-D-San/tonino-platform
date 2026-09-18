package it.asansonne.common.jpa.model;

import it.asansonne.common.core.model.Models;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
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
  @Column(name = "uuid", columnDefinition = "UUID")
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

  @PrePersist
  protected void onCreate() {
    long now = System.currentTimeMillis();
    if (this.uuid == null) {
      this.uuid = UUID.randomUUID();
    }
    if (this.createdAt == null) {
      this.createdAt = now;
    }
    if (this.updatedAt == null) {
      this.updatedAt = now;
    }
    if (this.isActive == null) {
      this.isActive = true;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = System.currentTimeMillis();
  }

  public void activate() {
    this.isActive = true;
  }

  public void deactivate() {
    this.isActive = false;
  }
}
