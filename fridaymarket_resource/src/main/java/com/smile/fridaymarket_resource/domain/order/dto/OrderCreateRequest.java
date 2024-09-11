package com.smile.fridaymarket_resource.domain.order.dto;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderCreateRequest {

    @NotNull(message = "주문 형식은 필수 값입니다.")
    private OrderType orderType;

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private Long productId;

    @NotNull(message = "수량은 필수 값입니다.")
    @DecimalMin(value = "3.75", inclusive = true, message = "수량은 최소 3.75 이상이어야 합니다.")
    private BigDecimal quantity;

    @NotNull(message = "가격은 필수 값입니다.")
    @DecimalMin(value = "0.00", inclusive = true, message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;

    @NotBlank(message = "배송지는 필수 값입니다.")
    private String deliveryAddress;

}
