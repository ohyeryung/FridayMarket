package com.smile.fridaymarket_auth.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorCode {

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
