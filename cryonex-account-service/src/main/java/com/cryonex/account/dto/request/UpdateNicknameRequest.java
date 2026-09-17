package com.cryonex.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNicknameRequest {
    @NotBlank(message = "Nickname is mandatory")
    private String nickname;
}