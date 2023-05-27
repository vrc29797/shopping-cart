package com.merge.shoppingcart.service;

import com.merge.shoppingcart.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

  Product saveProduct(Product product);

  List<Product> listProducts();

  Product getProduct(UUID id);
}
