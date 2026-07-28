package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.CustomerRequestDto;
import com.cryonex.customer.dto.request.CustomerStatusUpdateRequestDto;
import com.cryonex.customer.dto.request.CustomerUpdateRequestDto;
import com.cryonex.customer.dto.response.CustomerCreateResponseDto;
import com.cryonex.customer.dto.response.CustomerResponseDto;
import com.cryonex.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createCustomer(@Valid @RequestBody CustomerRequestDto request) {
        CustomerCreateResponseDto response = customerService.createCustomer(request);
        return new ResponseEntity<>(
                ApiResponse.success("Customer created successfully.", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getCustomerDetails(@PathVariable String customerId) {
        CustomerResponseDto response = customerService.getCustomerDetails(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully.", response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchCustomers(@RequestParam(required = false) String customerId,
                                                       @RequestParam(required = false) String cif,
                                                       @RequestParam(required = false) String mobile,
                                                       @RequestParam(required = false) String pan,
                                                       @RequestParam(required = false) String aadhaar,
                                                       @RequestParam(required = false) String status,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerResponseDto> response = customerService.searchCustomers(customerId, cif, mobile, pan, aadhaar, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Customer search completed successfully.", response));
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCustomer(@PathVariable String customerId,
                                                      @RequestBody CustomerUpdateRequestDto request) {
        CustomerResponseDto response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully.", response));
    }

    @PutMapping("/{customerId}/status")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCustomerStatus(@PathVariable String customerId,
                                                            @Valid @RequestBody CustomerStatusUpdateRequestDto request) {
        customerService.updateCustomerStatus(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer status updated successfully.", null));
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer Deactivated Successfully.", null));
    }

}