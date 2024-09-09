package com.smile.fridaymarket_auth.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ILLEGAL_USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 가입된 아이디입니다."),
    ILLEGAL_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "가입되지 않은 아이디입니다."),
    ILLEGAL_PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ILLEGAL_REFRESH_TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 refreshToken 입니다.");
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
