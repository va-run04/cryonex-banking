package com.cryonex.customer.exception;

public class UnauthorisedException extends RuntimeException{

    private final String errorCode;

    public UnauthorisedException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
