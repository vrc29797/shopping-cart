package com.merge.shoppingcart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class Product extends BaseEntity {
  @Column(name = "name")
  String name;

  // Can be made ENUM in the future as per requirement.
  // Can also add subCategory Column in the future.
  @Column(name = "category")
  String category;

  @Column(name = "description")
  String description;

  @Column(name = "is_active")
  boolean isActive;

  @Column(name = "stock")
  int stock;

  @Column(name = "price")
  int price;
}
