package com.merge.shoppingcart.repo;

import com.merge.shoppingcart.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {}
