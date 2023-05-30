package com.merge.shoppingcart.service.impl;

import static com.merge.shoppingcart.dto.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.merge.shoppingcart.dto.Cart;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.service.ProductService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock private ProductService productService;

  @InjectMocks private OrderServiceImpl orderService;

  @Test
  void testAddToCart_NormalCase() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");
    product.setStock(10);
    product.setPrice(10);

    when(productService.getProduct(productId)).thenReturn(product);

    Cart cart = orderService.addToCart(productId, 3, "user@example.com");

    assertNotNull(cart);
    assertEquals("user@example.com", cart.getEmail());
    assertEquals(1, cart.getProducts().size());
    assertTrue(cart.getProducts().containsKey(productId));
    assertEquals(3, cart.getProducts().get(productId));
    assertEquals(BigDecimal.valueOf(30), cart.getTotalAmount());
    assertEquals(3, cart.getTotalQuantity());

    verify(productService, times(1)).getProduct(productId);
    verify(productService, times(1)).saveProduct(product);
  }

  @Test
  void testAddToCart_QuantityLessThanOne() {
    UUID productId = UUID.randomUUID();

    ApiException exception =
        assertThrows(
            ApiException.class, () -> orderService.addToCart(productId, 0, "user@example.com"));

    assertEquals(TOO_LOW_QUANTITY, exception.getErrorCode());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verifyNoInteractions(productService);
  }

  @Test
  void testAddToCart_ProductOutOfStock() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");
    product.setStock(5);

    when(productService.getProduct(productId)).thenReturn(product);

    ApiException exception =
        assertThrows(
            ApiException.class, () -> orderService.addToCart(productId, 10, "user@example.com"));

    assertEquals(PRODUCT_OUT_OF_STOCK, exception.getErrorCode());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());

    verify(productService, times(1)).getProduct(productId);
    verifyNoMoreInteractions(productService);
  }

  @Test
  void testRemoveFromCart_NormalCase() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");
    product.setStock(5);
    product.setPrice(10);

    Map<String, Cart> activeUserCarts = new HashMap<>();
    Cart cart = new Cart("user@example.com");
    cart.getProducts().put(productId, 5);
    cart.setTotalAmount(BigDecimal.valueOf(50.0));
    cart.setTotalQuantity(5);
    activeUserCarts.put("user@example.com", cart);

    when(productService.getProduct(productId)).thenReturn(product);

    orderService.activeUserCarts = activeUserCarts;
    Cart updatedCart = orderService.removeFromCart(productId, 3, "user@example.com");

    assertNotNull(updatedCart);
    assertEquals("user@example.com", updatedCart.getEmail());
    assertEquals(1, updatedCart.getProducts().size());
    assertTrue(updatedCart.getProducts().containsKey(productId));
    assertEquals(2, updatedCart.getProducts().get(productId));
    assertEquals(BigDecimal.valueOf(20.0), updatedCart.getTotalAmount());
    assertEquals(2, updatedCart.getTotalQuantity());

    verify(productService, times(1)).getProduct(productId);
    verify(productService, times(1)).saveProduct(product);
  }

  @Test
  void testRemoveFromCart_QuantityLessThanOne() {
    UUID productId = UUID.randomUUID();

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> orderService.removeFromCart(productId, 0, "user@example.com"));

    assertEquals(TOO_LOW_QUANTITY, exception.getErrorCode());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verifyNoInteractions(productService);
  }

  @Test
  void testRemoveFromCart_CartNotFound() {
    UUID productId = UUID.randomUUID();

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> orderService.removeFromCart(productId, 1, "user@example.com"));

    assertEquals(CART_NOT_FOUND, exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

    verifyNoInteractions(productService);
  }

  @Test
  void testRemoveFromCart_ProductNotInCart() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");

    Map<String, Cart> activeUserCarts = new HashMap<>();
    Cart cart = new Cart("user@example.com");
    activeUserCarts.put("user@example.com", cart);

    when(productService.getProduct(productId)).thenReturn(product);
    orderService.activeUserCarts = activeUserCarts;

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> orderService.removeFromCart(productId, 1, "user@example.com"));

    assertEquals(PRODUCT_NOT_IN_CART, exception.getErrorCode());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verify(productService, times(1)).getProduct(productId);
    verifyNoMoreInteractions(productService);
  }

  @Test
  void testRemoveFromCart_QuantityGreaterThanExisting() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");

    Map<String, Cart> activeUserCarts = new HashMap<>();
    Cart cart = new Cart("user@example.com");
    cart.getProducts().put(productId, 3);
    activeUserCarts.put("user@example.com", cart);

    when(productService.getProduct(productId)).thenReturn(product);

    orderService.activeUserCarts = activeUserCarts;

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> orderService.removeFromCart(productId, 5, "user@example.com"));

    assertEquals(TOO_BIG_QUANTITY, exception.getErrorCode());
    assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

    verify(productService, times(1)).getProduct(productId);
    verifyNoMoreInteractions(productService);
  }

  @Test
  void testGetCart_NormalCase() {
    Map<String, Cart> activeUserCarts = new HashMap<>();
    Cart cart = new Cart("user@example.com");
    activeUserCarts.put("user@example.com", cart);

    orderService.activeUserCarts = activeUserCarts;

    Cart retrievedCart = orderService.getCart("user@example.com");

    assertNotNull(retrievedCart);
    assertEquals("user@example.com", retrievedCart.getEmail());
  }

  @Test
  void testGetCart_CartNotFound() {
    ApiException exception =
        assertThrows(ApiException.class, () -> orderService.getCart("user@example.com"));

    assertEquals(CART_NOT_FOUND, exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }
}
