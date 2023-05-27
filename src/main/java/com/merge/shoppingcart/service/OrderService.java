package com.merge.shoppingcart.service;

import com.merge.shoppingcart.dto.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> listProducts();

    Product getProduct(UUID id);

}
