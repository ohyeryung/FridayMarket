package com.smile.fridaymarket_resource.domain.price.service;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.price.entity.Price;

import java.math.BigDecimal;

public interface PriceReader {

    Price getPrice(BigDecimal price, OrderType orderType, String userId);

}
