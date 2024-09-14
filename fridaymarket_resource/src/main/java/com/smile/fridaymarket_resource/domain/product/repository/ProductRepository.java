package com.smile.fridaymarket_resource.domain.product.repository;

import com.smile.fridaymarket_resource.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
