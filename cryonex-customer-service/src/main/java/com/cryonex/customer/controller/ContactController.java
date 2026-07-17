package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.ContactRequestDto;
import com.cryonex.customer.dto.request.EmailUpdateRequestDto;
import com.cryonex.customer.dto.request.MobileUpdateRequestDto;
import com.cryonex.customer.dto.request.PreferredContactModeUpdateRequestDto;
import com.cryonex.customer.dto.response.ContactResponseDto;
import com.cryonex.customer.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // Adds contact details for a customer
    @PostMapping
    public ResponseEntity<ApiResponse> addContact(@PathVariable String customerId,
                                                  @Valid @RequestBody ContactRequestDto request) {
        ContactResponseDto response = contactService.addContact(customerId, request);
        return new ResponseEntity<>(
                ApiResponse.success("Customer contact details added successfully.", response),
                HttpStatus.CREATED
        );
    }

    // Retrieves a customer's contact details
    @GetMapping
    public ResponseEntity<ApiResponse> getContactDetails(@PathVariable String customerId) {
        ContactResponseDto response = contactService.getContactDetails(customerId);
        return ResponseEntity.ok(ApiResponse.success("Contact details retrieved successfully.", response));
    }

    // Updates the customer's mobile number
    @PutMapping("/mobile")
    public ResponseEntity<ApiResponse> updateMobile(@PathVariable String customerId,
                                                    @Valid @RequestBody MobileUpdateRequestDto request) {
        contactService.updateMobile(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Mobile number updated successfully.", null));
    }

    // Updates the customer's email address
    @PutMapping("/email")
    public ResponseEntity<ApiResponse> updateEmail(@PathVariable String customerId,
                                                   @Valid @RequestBody EmailUpdateRequestDto request) {
        contactService.updateEmail(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Email updated successfully.", null));
    }

    // Updates the customer's preferred contact mode
    @PutMapping("/preferred-mode")
    public ResponseEntity<ApiResponse> updatePreferredContactMode(@PathVariable String customerId,
                                                                  @Valid @RequestBody PreferredContactModeUpdateRequestDto request) {
        contactService.updatePreferredContactMode(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Preferred contact mode updated successfully.", null));
    }

}