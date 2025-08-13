package com.supshop.suppingmall.api.common;

public record ApiResponse<T>(boolean success, T data, ApiError error, String traceId) {
    public static <T> ApiResponse<T> ok(T data, String traceId) {
        return new ApiResponse<>(true, data, null, traceId);
    }
    public static ApiResponse<Void> fail(ApiError error, String traceId) {
        return new ApiResponse<>(false, null, error, traceId);
    }
}