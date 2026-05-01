package it.asansonne.ala.commonlib.ccsr.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Models {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  @ToString.Exclude
  private Long id;

  @Column(name = "uuid", nullable = false, unique = true, columnDefinition = "UUID")
  private UUID uuid;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  protected Long createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  protected Long updatedAt;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @PrePersist
  public void prePersist() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
    }
    if (isActive == null) {
      isActive = true;
    }
  }
}
