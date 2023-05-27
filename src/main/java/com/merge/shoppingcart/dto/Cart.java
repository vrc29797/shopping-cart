package com.merge.shoppingcart.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class Cart {
  UUID userId;
  // ProductId, Quantity
  Map<UUID, Integer> products;
  BigDecimal totalAmount;
}
