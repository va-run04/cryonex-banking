package com.cryonex.customer.dto.response;

import com.cryonex.customer.enums.DocumentType;
import com.cryonex.customer.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentResponseDto {

    private String documentId;
    private DocumentType documentType;
    private String documentNumber;
    private VerificationStatus verificationStatus;
    private LocalDateTime uploadedDate;

}