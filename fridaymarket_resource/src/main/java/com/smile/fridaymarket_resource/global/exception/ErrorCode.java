package com.smile.fridaymarket_resource.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Token
    TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 주문입니다."),
    ILLEGAL_ORDER_USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "본인의 주문만 조회가 가능합니다."),
    ORDER_STATUS_NOT_ORDER_COMPLETED(HttpStatus.BAD_REQUEST, "입금 완료 상태로 변경 불가한 주문입니다."),
    ORDER_STATUS_NOT_PAYMENT_RECEIVED(HttpStatus.BAD_REQUEST, "발송 완료 상태로 변경 불가한 주문입니다."),
    ORDER_STATUS_NOT_RECEIVED(HttpStatus.BAD_REQUEST, "수령 완료 상태로 변경 불가한 주문입니다."),
    ORDER_STATUS_NOT_PAYMENT_SENT(HttpStatus.BAD_REQUEST, "송금 완료 상태로 변경 불가한 주문입니다."),
    CANCEL_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "주문 취소 요청을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
