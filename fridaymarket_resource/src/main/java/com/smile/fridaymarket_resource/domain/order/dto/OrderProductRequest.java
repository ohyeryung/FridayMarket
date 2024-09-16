package com.smile.fridaymarket_resource.domain.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderProductRequest {

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private Long productId;

    @NotNull(message = "수량은 필수 값입니다.")
    @DecimalMin(value = "3.75", inclusive = true, message = "수량은 최소 3.75 이상이어야 합니다.")
    private BigDecimal quantity;

    @NotNull(message = "가격은 필수 값입니다.")
    @DecimalMin(value = "0.00", inclusive = true, message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;

}
