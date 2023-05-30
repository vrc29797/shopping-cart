package com.merge.shoppingcart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.merge.shoppingcart.dto.*;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.service.ProductService;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  @Mock private ProductService productService;

  @InjectMocks private ProductController productController;

  @Test
  void createProduct_WithValidRequest_ShouldReturnCreatedProduct() {
    // Prepare
    ProductRequest validRequest =
        ProductRequest.builder()
            .name("New Product")
            .category("Electronics")
            .description("New product")
            .active(true)
            .stock(10)
            .price(100)
            .build();
    Product createdProduct =
        Product.builder()
            .name(validRequest.getName())
            .category(validRequest.getCategory())
            .description(validRequest.getDescription())
            .stock(validRequest.getStock())
            .price(validRequest.getPrice())
            .isActive(validRequest.isActive())
            .build();
    when(productService.saveProduct(any(Product.class))).thenReturn(createdProduct);

    // Execute
    ResponseEntity<BaseResponse<Product>> responseEntity =
        productController.addProduct(validRequest);

    // Verify
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    BaseResponse<Product> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertNull(response.getError());
    assertNull(response.getErrorCode());

    Product Product = response.getData();
    assertEquals(createdProduct.getId(), Product.getId());
    assertEquals(createdProduct.getName(), Product.getName());
    assertEquals(createdProduct.getCategory(), Product.getCategory());
    assertEquals(createdProduct.getDescription(), Product.getDescription());
    assertEquals(createdProduct.isActive(), Product.isActive());
    assertEquals(createdProduct.getStock(), Product.getStock());
    assertEquals(createdProduct.getPrice(), Product.getPrice());
  }

  @Test
  void listProducts_WithNoProducts_ShouldReturnEmptyList() {
    // Prepare
    when(productService.listProducts()).thenReturn(Collections.emptyList());

    // Execute
    ResponseEntity<BaseResponse<List<Product>>> responseEntity = productController.getAllProducts();

    // Verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    BaseResponse<List<Product>> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertTrue(response.getData().isEmpty());
    assertNull(response.getError());
    assertNull(response.getErrorCode());
  }

  @Test
  void listProducts_WithExistingProducts_ShouldReturnProductList() {
    // Prepare
    List<Product> existingProducts =
        Arrays.asList(
            new Product("Product 1", "Electronics", "Product 1 description", true, 10, 100),
            new Product("Product 2", "Clothing", "Product 2 description", true, 5, 50));
    when(productService.listProducts()).thenReturn(existingProducts);

    // Execute
    ResponseEntity<BaseResponse<List<Product>>> responseEntity = productController.getAllProducts();

    // Verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    BaseResponse<List<Product>> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertNotNull(response.getData());
    assertEquals(existingProducts.size(), response.getData().size());
    assertNull(response.getError());
    assertNull(response.getErrorCode());

    List<Product> productResponses = response.getData();
    for (int i = 0; i < existingProducts.size(); i++) {
      Product existingProduct = existingProducts.get(i);
      Product Product = productResponses.get(i);
      assertEquals(existingProduct.getName(), Product.getName());
      assertEquals(existingProduct.getCategory(), Product.getCategory());
      assertEquals(existingProduct.getDescription(), Product.getDescription());
      assertEquals(existingProduct.isActive(), Product.isActive());
      assertEquals(existingProduct.getStock(), Product.getStock());
      assertEquals(existingProduct.getPrice(), Product.getPrice());
    }
  }
}
