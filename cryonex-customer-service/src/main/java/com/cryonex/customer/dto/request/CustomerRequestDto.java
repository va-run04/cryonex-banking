package com.cryonex.customer.dto.request;
import com.cryonex.customer.enums.CustomerCategory;
import com.cryonex.customer.enums.CustomerType;
import com.cryonex.customer.enums.Gender;
import com.cryonex.customer.enums.MaritalStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CustomerRequestDto {

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    private String middleName;

    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is mandatory")
    private Gender gender;

    private MaritalStatus maritalStatus;

    private String occupation;

    private BigDecimal annualIncome;

    @NotNull(message = "Customer type is mandatory")
    private CustomerType customerType;

    @NotNull(message = "Customer category is mandatory")
    private CustomerCategory customerCategory;

    @NotBlank(message = "PAN number is mandatory")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "Invalid PAN format")
    private String panNumber;

    @NotBlank(message = "Aadhaar number is mandatory")
    @Pattern(regexp = "\\d{12}", message = "Aadhaar must be 12 digits")
    private String aadhaarNumber;

    private String nationality;

}