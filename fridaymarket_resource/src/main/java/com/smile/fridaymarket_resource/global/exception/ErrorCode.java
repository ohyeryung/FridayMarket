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
    ILLEGAL_ORDER_USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "본인의 주문만 조회가 가능합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
