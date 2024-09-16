package com.smile.fridaymarket_resource.domain.price.service;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.price.entity.Price;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PriceStore {

    Price saveNewPrice(BigDecimal price, OrderType orderType, String userId);

}
