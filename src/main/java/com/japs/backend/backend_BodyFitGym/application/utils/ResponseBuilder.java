package com.japs.backend.backend_BodyFitGym.application.utils;


import com.japs.backend.backend_BodyFitGym.application.response.ApiResponse;

public class ResponseBuilder {

    public static <T> ApiResponse<T> successMessage(String message, T data) {
        return ApiResponse.<T>builder().message(message).status(true).data(data).build();
    }

    public static ApiResponse<Void> successMessage(String message) {
        return ApiResponse.<Void>builder().message(message).status(true).data(null).build();
    }

    public static <T> ApiResponse<T> errorMessage(String message,T data) {
        return ApiResponse.<T>builder().message(message).status(false).data(data).build();
    }

    public static ApiResponse<Void> errorMessage(String message) {
        return ApiResponse.<Void>builder().message(message).status(false).data(null).build();
    }

}
