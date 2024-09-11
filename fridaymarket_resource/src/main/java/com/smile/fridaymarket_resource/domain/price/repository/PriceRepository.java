package com.smile.fridaymarket_resource.domain.price.repository;

import com.smile.fridaymarket_resource.domain.price.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
