package com.merge.shoppingcart.controller;

import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.dto.ProductRequest;
import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired ProductService productService;

  @PostMapping("/add")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<Product>> addProduct(
      @RequestBody ProductRequest productRequest) {
    Product product =
        Product.builder()
            .name(productRequest.getName())
            .price(productRequest.getPrice())
            .description(productRequest.getDescription())
            .category(productRequest.getCategory())
            .stock(productRequest.getStock())
            .isActive(productRequest.isActive())
            .build();

    Product response = productService.saveProduct(product);
    return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<BaseResponse<List<Product>>> getAllProducts() {
    List<Product> response = productService.listProducts();
    return ResponseEntity.ok(new BaseResponse<>(response));
  }
}
