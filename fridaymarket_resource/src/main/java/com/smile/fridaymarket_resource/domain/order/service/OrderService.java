package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.dto.OrderPaging;
import com.smile.fridaymarket_resource.domain.order.dto.OrderResponse;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public interface OrderService {
    void createOrder(String userId, OrderCreateRequest request);

    void isPaymentReceived(String userId, Long orderId);

    void isShipped(Long orderId);

    void isReceived(Long orderId);

    void isPaymentSent(Long orderId);

    void cancelRequestByUser(String userId, Long orderId);

    void cancelOrder(Long orderId);

    OrderResponse getOrderInvoice(String userId, Long orderId);

    OrderPaging getOrderList(String userId, Pageable pageable);

}
