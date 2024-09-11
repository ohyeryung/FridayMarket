package com.smile.fridaymarket_resource.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductName {
    GOLD_999("GOLD 99.9%"),
    GOLD_9999("GOLD 99.99%");

    private final String productName;
}
