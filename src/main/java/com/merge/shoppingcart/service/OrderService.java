package com.merge.shoppingcart.service;

import com.merge.shoppingcart.dto.Cart;
import java.util.UUID;

public interface OrderService {

  Cart addToCart(UUID productId, int quantity, String email);

  Cart removeFromCart(UUID productId, int quantity, String email);

  Cart getCart(String email);
}
