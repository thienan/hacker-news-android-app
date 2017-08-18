package com.marcelljee.hackernews.loader;

public class AppResponse<T> {

    private T response;
    private String errorMessage;

    public static <T> AppResponse ok(T response) {
        return new AppResponse(response);
    }

    public static AppResponse error(String errorMessage) {
        return new AppResponse(errorMessage);
    }

    private AppResponse(T response) {
        this.response = response;
    }

    private AppResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return response != null;
    }

    public T getData() {
        return response;
    }

    @SuppressWarnings("unused")
    public String getErrorMessage() {
        return errorMessage;
    }
}
