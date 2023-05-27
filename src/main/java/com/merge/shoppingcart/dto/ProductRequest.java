package com.merge.shoppingcart.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class ProductRequest {

  String name;

  String category;

  String description;

  boolean isActive;

  int stock;

  int price;
}
