package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.Relationship;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class NomineeRequestDto {

    @NotBlank(message = "Nominee name is mandatory")
    private String nomineeName;

    @NotNull(message = "Relationship is mandatory")
    private Relationship relationship;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotNull(message = "Share percentage is mandatory")
    @DecimalMin(value = "0.01", message = "Share percentage must be greater than 0")
    @DecimalMax(value = "100.0", message = "Share percentage cannot exceed 100")
    private BigDecimal sharePercentage;

}