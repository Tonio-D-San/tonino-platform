package it.asansonne.ala.commonlib.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseResponse implements Response {
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Schema(
      description = "Uuid",
      name = "uuid",
      type = "UUID",
      example = "08fba211-60ca-45fc-b809-86bc2ad81dca")
  private UUID uuid;

  @Schema(
      description = "Last update",
      name = "updateAt",
      type = "Long",
      example = "1769300745474"
  )
  private Long updatedAt;

}
