package com.smile.fridaymarket_resource.domain.order.dto;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderStatus;
import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderNo;

    private UUID userId;

    private OrderType orderType;

    private OrderStatus orderStatus;

    private List<OrderProductResponse> productResponses;

    private BigDecimal amount;

    private String deliveryAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private boolean isDeleted;

}

