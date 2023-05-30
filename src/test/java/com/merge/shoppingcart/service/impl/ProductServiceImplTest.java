package com.merge.shoppingcart.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.merge.shoppingcart.dto.ErrorCode;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.repo.ProductRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock private ProductRepo productRepo;

  @InjectMocks private ProductServiceImpl productService;

  @Test
  void testSaveProduct_NormalCase() {
    Product product = new Product();
    product.setName("Product 1");

    when(productRepo.save(any(Product.class))).thenReturn(product);

    Product savedProduct = productService.saveProduct(product);

    assertNotNull(savedProduct);
    assertEquals("Product 1", savedProduct.getName());
  }

  @Test
  void testGetProduct_NormalCase() {
    UUID productId = UUID.randomUUID();
    Product product = new Product();
    product.setId(productId);
    product.setName("Product 1");

    when(productRepo.findById(productId)).thenReturn(Optional.of(product));

    Product retrievedProduct = productService.getProduct(productId);

    assertNotNull(retrievedProduct);
    assertEquals(productId, retrievedProduct.getId());
    assertEquals("Product 1", retrievedProduct.getName());
  }

  @Test
  void testGetProduct_ProductNotFound() {
    UUID productId = UUID.randomUUID();

    when(productRepo.findById(productId)).thenReturn(Optional.empty());

    ApiException exception =
        assertThrows(ApiException.class, () -> productService.getProduct(productId));
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void testListProducts_NormalCase() {
    Product product1 = new Product();
    product1.setId(UUID.randomUUID());
    product1.setName("Product 1");

    Product product2 = new Product();
    product2.setId(UUID.randomUUID());
    product2.setName("Product 2");

    List<Product> productList = new ArrayList<>();
    productList.add(product1);
    productList.add(product2);

    when(productRepo.findAll()).thenReturn(productList);

    List<Product> retrievedProducts = productService.listProducts();

    assertNotNull(retrievedProducts);
    assertEquals(2, retrievedProducts.size());
    assertEquals("Product 1", retrievedProducts.get(0).getName());
    assertEquals("Product 2", retrievedProducts.get(1).getName());
  }
}
