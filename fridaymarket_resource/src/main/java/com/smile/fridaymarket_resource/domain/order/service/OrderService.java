package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void createOrder(String userId, OrderCreateRequest request);

    void isPaymentReceived(String userId, Long orderId);

}
