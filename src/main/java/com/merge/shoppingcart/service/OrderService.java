package com.merge.shoppingcart.service;

import com.merge.shoppingcart.dto.Cart;
import java.util.UUID;

public interface OrderService {

  Cart addToCart(UUID productId, int quantity, UUID userId) throws Exception;

  Cart removeFromCart(UUID productId, int quantity, UUID userId) throws Exception;

  Cart getCart(UUID userId) throws Exception;
}
