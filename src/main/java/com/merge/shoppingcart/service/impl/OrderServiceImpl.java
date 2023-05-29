package com.merge.shoppingcart.service.impl;

import static com.merge.shoppingcart.dto.ErrorCode.*;

import com.merge.shoppingcart.dto.Cart;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.service.OrderService;
import com.merge.shoppingcart.service.ProductService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired ProductService productService;

  // Maintaining Cart in memory for now, Can do it in DB in the future.
  Map<String, Cart> activeUserCarts = new HashMap<>();

  @Override
  public Cart addToCart(UUID productId, int quantity, String email) {
    if (quantity < 1) throw new ApiException(TOO_LOW_QUANTITY, HttpStatus.BAD_REQUEST);

    Product product = productService.getProduct(productId);

    Cart cart;
    if (activeUserCarts.containsKey(email)) {
      cart = activeUserCarts.get(email);
    } else {
      cart = new Cart(email);
      activeUserCarts.put(email, cart);
    }

    Map<UUID, Integer> existingProducts = cart.getProducts();

    if (product.getStock() >= quantity) {

      if (existingProducts.containsKey(productId)) {
        existingProducts.put(productId, existingProducts.get(productId) + quantity);
      } else existingProducts.put(productId, quantity);

      cart.setProducts(existingProducts);
      cart.setTotalAmount(
          cart.getTotalAmount().add(BigDecimal.valueOf((long) quantity * product.getPrice())));
      cart.setTotalQuantity(cart.getTotalQuantity() + quantity);

      // Ideally this should be at order checkout -  after payment is confirmed,
      // since we are not going to make checkout, I've kept it here.
      product.setStock(product.getStock() - quantity);
      productService.saveProduct(product);

    } else throw new ApiException(PRODUCT_OUT_OF_STOCK, HttpStatus.INTERNAL_SERVER_ERROR);
    return cart;
  }

  @Override
  public Cart removeFromCart(UUID productId, int quantity, String email) {
    if (quantity < 1) throw new ApiException(TOO_LOW_QUANTITY, HttpStatus.BAD_REQUEST);

    if (!activeUserCarts.containsKey(email))
      throw new ApiException(CART_NOT_FOUND, HttpStatus.NOT_FOUND);

    Cart cart = activeUserCarts.get(email);
    Map<UUID, Integer> existingProducts = cart.getProducts();

    Product product = productService.getProduct(productId);

    if (existingProducts.containsKey(productId)) {
      if (existingProducts.get(productId) < quantity)
        throw new ApiException(TOO_BIG_QUANTITY, HttpStatus.BAD_REQUEST);

      int newQuantity = existingProducts.get(productId) - quantity;
      if (newQuantity == 0) existingProducts.remove(productId);
      else existingProducts.put(productId, existingProducts.get(productId) - quantity);

    } else throw new ApiException(PRODUCT_NOT_IN_CART, HttpStatus.BAD_REQUEST);
    cart.setProducts(existingProducts);
    cart.setTotalAmount(
        cart.getTotalAmount().subtract(BigDecimal.valueOf((long) quantity * product.getPrice())));
    cart.setTotalQuantity(cart.getTotalQuantity() - quantity);

    product.setStock(product.getStock() + quantity);
    productService.saveProduct(product);
    return cart;
  }

  @Override
  public Cart getCart(String email) {
    if (!activeUserCarts.containsKey(email))
      throw new ApiException(CART_NOT_FOUND, HttpStatus.NOT_FOUND);
    return activeUserCarts.get(email);
  }
}
