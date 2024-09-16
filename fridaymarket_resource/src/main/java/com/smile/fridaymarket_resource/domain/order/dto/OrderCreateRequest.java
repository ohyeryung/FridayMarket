package com.smile.fridaymarket_resource.domain.order.dto;

import com.smile.fridaymarket_resource.domain.order.entity.enums.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {

    @NotNull(message = "주문 형식은 필수 값입니다.")
    private OrderType orderType;

    @NotNull(message = "주문 상품 목록은 필수 값입니다.")
    private List<OrderProductRequest> orderProducts;

    @NotBlank(message = "배송지는 필수 값입니다.")
    private String deliveryAddress;

}
