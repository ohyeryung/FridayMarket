package com.smile.fridaymarket_resource.domain.order.repository;

import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
