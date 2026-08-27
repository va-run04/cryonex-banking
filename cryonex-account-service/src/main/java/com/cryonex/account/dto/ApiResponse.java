package com.cryonex.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class ApiResponse {

    private String status;
    private String errorCode;
    private String message;
    private Object data;

    // constructor that will be helpful for success
    public ApiResponse(String status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }


    //success
    public static ApiResponse success(String message, Object data){
        return new ApiResponse("SUCCESS", message, data);
    }

    //failed
    public static ApiResponse error(String errorCode, String message){
        ApiResponse response = new ApiResponse("FAILED", message, null);
        response.errorCode = errorCode;
        return response;
    }


}
