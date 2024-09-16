package com.smile.fridaymarket_resource.domain.order.service;

import com.smile.fridaymarket_resource.domain.order.dto.OrderCreateRequest;
import com.smile.fridaymarket_resource.domain.order.dto.OrderProductRequest;
import com.smile.fridaymarket_resource.domain.order.entity.OrderInvoice;
import com.smile.fridaymarket_resource.domain.order.entity.OrderProduct;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import com.smile.fridaymarket_resource.domain.product.entity.Product;

public interface OrderStore {

    OrderInvoice saveOrderInvoice(String userId, OrderCreateRequest request);

    void saveOrderInvoice(OrderInvoice orderInvoice);

    OrderProduct saveOrderProduct(OrderCreateRequest request, OrderInvoice orderInvoice, Product product, Price price, OrderProductRequest productRequest);

}
