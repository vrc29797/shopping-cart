package com.merge.shoppingcart.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class Cart {
  String email;
  // ProductId, Quantity
  Map<UUID, Integer> products;
  long totalQuantity;
  BigDecimal totalAmount;

  public Cart(String email) {
    this.email = email;
    this.products = new HashMap<>();
    this.totalAmount = BigDecimal.ZERO;
  }
}
