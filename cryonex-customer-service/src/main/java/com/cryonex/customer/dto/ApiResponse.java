package com.cryonex.customer.dto;

public class ApiResponse {

    private String status;
    private String errorCode;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse("SUCCESS", message, data);
    }

    public static ApiResponse error(String errorCode, String message) {
        ApiResponse response = new ApiResponse("FAILED", message, null);
        response.errorCode = errorCode;
        return response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}