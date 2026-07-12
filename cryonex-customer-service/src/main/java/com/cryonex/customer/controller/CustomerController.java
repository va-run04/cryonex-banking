package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.CustomerRequestDto;
import com.cryonex.customer.dto.request.CustomerStatusUpdateRequestDto;
import com.cryonex.customer.dto.request.CustomerUpdateRequestDto;
import com.cryonex.customer.dto.response.CustomerCreateResponseDto;
import com.cryonex.customer.dto.response.CustomerResponseDto;
import com.cryonex.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Creates a new customer record
    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@Valid @RequestBody CustomerRequestDto request) {
        CustomerCreateResponseDto response = customerService.createCustomer(request);
        return new ResponseEntity<>(
                ApiResponse.success("Customer created successfully.", response),
                HttpStatus.CREATED
        );
    }

    // Retrieves a customer's details by their ID
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerDetails(@PathVariable String customerId) {
        CustomerResponseDto response = customerService.getCustomerDetails(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully.", response));
    }

    // Updates a customer's editable fields
    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse> updateCustomer(@PathVariable String customerId,
                                                      @RequestBody CustomerUpdateRequestDto request) {
        CustomerResponseDto response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully.", response));
    }

    // Updates a customer's status (ACTIVE, INACTIVE, BLOCKED, DECEASED, DORMANT)
    @PutMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse> updateCustomerStatus(@PathVariable String customerId,
                                                            @Valid @RequestBody CustomerStatusUpdateRequestDto request) {
        customerService.updateCustomerStatus(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer status updated successfully.", null));
    }

    // Soft-deletes a customer by deactivating their record
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer Deactivated Successfully.", null));
    }

}