package com.smile.fridaymarket_resource.domain.order.repository.querydsl;

import com.smile.fridaymarket_resource.domain.order.dto.OrderList;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface OrderRepositoryQueryDsl {

    Page<OrderList> getOrderList(String userId, Pageable pageable);

}
