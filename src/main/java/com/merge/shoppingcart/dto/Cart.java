package com.merge.shoppingcart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
public class Cart {
    UUID userId;
    // ProductId, Quantity
    Map<UUID, Integer> products;
    BigDecimal totalAmount;
}
