package com.merge.shoppingcart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.dto.Cart;
import com.merge.shoppingcart.dto.CartRequest;
import com.merge.shoppingcart.service.OrderService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

  @Mock private OrderService orderService;

  @Mock private Authentication authentication;

  @InjectMocks private OrderController orderController;

  UUID productId = UUID.randomUUID();

  String email = "test@example.com";

  @Test
  void testAddToCart_NormalCase() {
    CartRequest cartRequest = new CartRequest();
    cartRequest.setId(productId);
    cartRequest.setQuantity(2);

    Cart expectedCart = new Cart(email);

    when(authentication.getName()).thenReturn(email);
    when(orderService.addToCart(productId, 2, email)).thenReturn(expectedCart);

    ResponseEntity<BaseResponse<Cart>> response =
        orderController.addToCart(cartRequest, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals(expectedCart, response.getBody().getData());
  }

  @Test
  void testRemoveFromCart_NormalCase() {
    CartRequest cartRequest = new CartRequest();
    cartRequest.setId(productId);
    cartRequest.setQuantity(2);

    Cart expectedCart = new Cart(email);

    when(authentication.getName()).thenReturn(email);
    when(orderService.removeFromCart(productId, 2, email)).thenReturn(expectedCart);

    ResponseEntity<BaseResponse<Cart>> response =
        orderController.removeFromCart(cartRequest, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals(expectedCart, response.getBody().getData());
  }

  @Test
  void testGetCart_NormalCase() {
    Cart expectedCart = new Cart(email);

    when(authentication.getName()).thenReturn(email);
    when(orderService.getCart(email)).thenReturn(expectedCart);

    ResponseEntity<BaseResponse<Cart>> response = orderController.getCart(authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals(expectedCart, response.getBody().getData());
  }
}
