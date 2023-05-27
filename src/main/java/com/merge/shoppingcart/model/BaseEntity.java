package com.merge.shoppingcart.model;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void setId(UUID id) {
    this.id = id;
  }

  @PrePersist
  protected void onSave() {
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
