package com.cryonex.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Role is mandatory")
    private String role;

}