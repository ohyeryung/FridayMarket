package com.smile.fridaymarket_resource.global.response;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    public boolean success; // 기본 값은 false
    public String message; // 예외 메세지
    public HttpStatus httpStatus; // Http 상태 값 400, 404, 500 등

    public ErrorResponse() {
        this.success = false;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.success = false;
        this.httpStatus = status;
        this.message = message;
    }

    static public ErrorResponse create() {

        return new ErrorResponse();
    }

    public ErrorResponse message(String message) {

        this.message = message;
        return this;
    }

    public ErrorResponse httpStatus(HttpStatus httpStatus) {

        this.httpStatus = httpStatus;
        return this;
    }

}
