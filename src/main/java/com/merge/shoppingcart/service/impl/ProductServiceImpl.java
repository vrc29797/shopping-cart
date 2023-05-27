package com.merge.shoppingcart.service.impl;

import com.merge.shoppingcart.model.Product;
import com.merge.shoppingcart.repo.ProductRepo;
import com.merge.shoppingcart.service.ProductService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired ProductRepo productRepo;

  @Override
  public Product saveProduct(Product product) {
    return productRepo.save(product);
  }

  @Override
  public Product getProduct(UUID id) {
    return productRepo.findById(id).orElse(null);
  }

  @Override
  public List<Product> listProducts() {
    return productRepo.findAll();
  }
}
