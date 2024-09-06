package com.smile.fridaymarket_auth.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse<T> {

    private Result result;
    private String message;
    private T data;

    private static <T> SuccessResponse<T> success(T data, String message) {
        return SuccessResponse.<T>builder()
                .result(Result.SUCCESS)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> SuccessResponse<T> success(T data) {
        return success(data, "OK");
    }

    public static <T> SuccessResponse<T> successWithNoData(String message) {
        return success(null, message);
    }

    public enum Result {
        SUCCESS, FAIL
    }
}
