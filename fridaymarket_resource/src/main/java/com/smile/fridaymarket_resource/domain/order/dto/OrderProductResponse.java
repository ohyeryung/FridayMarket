package com.smile.fridaymarket_resource.domain.order.dto;

import com.smile.fridaymarket_resource.domain.product.entity.ProductName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponse {

    private ProductName productName;

    private BigDecimal price;

    private BigDecimal quantity;
}
