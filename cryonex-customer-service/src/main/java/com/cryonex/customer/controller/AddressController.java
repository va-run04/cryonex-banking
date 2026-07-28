package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.AddressRequestDto;
import com.cryonex.customer.dto.request.AddressUpdateRequestDto;
import com.cryonex.customer.dto.response.AddressResponseDto;
import com.cryonex.customer.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addAddress(@PathVariable String customerId,
                                                  @Valid @RequestBody AddressRequestDto request) {
        AddressResponseDto response = addressService.addAddress(customerId, request);
        return new ResponseEntity<>(
                ApiResponse.success("Customer address added successfully.", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAddresses(@PathVariable String customerId) {
        List<AddressResponseDto> response = addressService.getAddresses(customerId);
        return ResponseEntity.ok(ApiResponse.success("Addresses retrieved successfully.", response));
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable String customerId,
                                                     @PathVariable String addressId,
                                                     @RequestBody AddressUpdateRequestDto request) {
        AddressResponseDto response = addressService.updateAddress(customerId, addressId, request);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully.", response));
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable String customerId,
                                                     @PathVariable String addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.ok(ApiResponse.success("Customer address deleted successfully.", null));
    }

}