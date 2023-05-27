package com.merge.shoppingcart.service.impl;

import static com.merge.shoppingcart.dto.ErrorCode.CART_NOT_FOUND;
import static com.merge.shoppingcart.dto.ErrorCode.PRODUCT_OUT_OF_STOCK;

import com.merge.shoppingcart.dto.Cart;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.service.OrderService;
import com.merge.shoppingcart.service.ProductService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired ProductService productService;

  // Maintaining Cart in memory for now, Can do it in DB in the future.
  Map<UUID, Cart> activeUserCarts = new HashMap<>();

  @Override
  public Cart addToCart(UUID productId, int quantity, UUID userId) {
    Cart cart = activeUserCarts.getOrDefault(userId, new Cart());
    Map<UUID, Integer> existingProducts = cart.getProducts();
    Product product = productService.getProduct(productId);
    if (product.getStock() >= quantity) {

      if (existingProducts.containsKey(productId)) {
        existingProducts.put(productId, existingProducts.get(productId) + quantity);
      } else existingProducts.put(productId, quantity);

      cart.setProducts(existingProducts);

      product.setStock(product.getStock() - quantity);
      productService.saveProduct(product);
    } else throw new ApiException(PRODUCT_OUT_OF_STOCK.name());
    return cart;
  }

  @Override
  public Cart removeFromCart(UUID productId, int quantity, UUID userId) {
    if (!activeUserCarts.containsKey(userId)) throw new ApiException(CART_NOT_FOUND.name());

    Cart cart = activeUserCarts.get(userId);
    Map<UUID, Integer> existingProducts = cart.getProducts();
    Product product = productService.getProduct(productId);
    if (existingProducts.containsKey(productId)) {
      existingProducts.put(productId, existingProducts.get(productId) - quantity);
    } else existingProducts.put(productId, quantity);
    cart.setProducts(existingProducts);

    product.setStock(product.getStock() + quantity);
    productService.saveProduct(product);
    return cart;
  }

  @Override
  public Cart getCart(UUID userId) {
    if (!activeUserCarts.containsKey(userId)) throw new ApiException(CART_NOT_FOUND.name());
    return activeUserCarts.get(userId);
  }
}
