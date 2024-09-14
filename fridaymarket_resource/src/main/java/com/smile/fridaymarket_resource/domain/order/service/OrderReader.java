package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;

public interface OrderReader {

    OrderInvoice getOrderInvoice(Long orderId);
}
