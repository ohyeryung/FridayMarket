package com.smile.fridaymarket_resource.domain.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    ORDER_COMPLETE("주문 완료"),
    PAYMENT_RECEIVED("입금 완료"),
    SHIPPED("발송 완료"),
    PAYMENT_SENT("송금 완료"),
    RECEIVED("수령 완료"),
    CANCELED("주문취소");

    private final String status;
}
