package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.product.entity.Product;

import java.math.BigDecimal;

public interface OrderStore {
    OrderInvoice saveOrderInvoice(String userId, OrderCreateRequest request, Product product, BigDecimal price);

    void saveOrderProduct(OrderCreateRequest request, OrderInvoice orderInvoice, Product product, BigDecimal price);

}
