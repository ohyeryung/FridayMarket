package com.smile.fridaymarket_resource.domain.order.dto;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import com.smile.fridaymarket_resource.domain.product.entity.ProductName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderList {

    private Long orderId;
    private OrderType orderType;
    private ProductName productName;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

}
