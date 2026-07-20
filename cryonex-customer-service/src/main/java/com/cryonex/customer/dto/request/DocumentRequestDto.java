package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentRequestDto {

    @NotNull(message = "Document type is mandatory")
    private DocumentType documentType;

    @NotBlank(message = "Document number is mandatory")
    private String documentNumber;

}