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

  boolean active;

  int stock;

  int price;
}
