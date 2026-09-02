package com.cryonex.account.exception;

public class BusinessValidationException extends RuntimeException{

    private final String errorCode;

    public BusinessValidationException(String errorCode, String message){
        // this is coming from throwable class in the hirarchy -> BusinessValidationException--> RunTimeException
        // --> Exception --> throwable class
        super(message);
        this.errorCode = errorCode; // private field
    }

    public String getErrorCode(){
        return errorCode;
    }
}
