package com.smile.fridaymarket_resource.domain.price.repository;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {
    
    Optional<Price> findByPriceAndOrderType(BigDecimal price, OrderType orderType);
}
