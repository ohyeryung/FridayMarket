package com.smile.fridaymarket_resource.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    BUY("구매주문"),
    SELL("판매주문");

    private final String name;

}
