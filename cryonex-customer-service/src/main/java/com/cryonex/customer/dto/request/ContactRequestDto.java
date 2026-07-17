package com.cryonex.customer.dto.request;

import com.cryonex.customer.enums.ContactMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequestDto {

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Pattern(regexp = "\\d{10}", message = "Alternate mobile must be 10 digits")
    private String alternateMobile;

    @Email(message = "Invalid email format")
    private String email;

    private String landline;

    @NotNull(message = "Preferred contact mode is mandatory")
    private ContactMode preferredContactMode;

}