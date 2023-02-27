package com.example.demotest.repository;

import com.example.demotest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product>findByProductName(String productName);
}
