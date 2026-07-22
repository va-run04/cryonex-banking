package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.KycRejectRequestDto;
import com.cryonex.customer.dto.request.KycResubmitRequestDto;
import com.cryonex.customer.dto.request.KycVerifyRequestDto;
import com.cryonex.customer.dto.response.KycResponseDto;
import com.cryonex.customer.service.KycService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/kyc")
public class KycController {

    private final KycService kycService;

    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    // Verifies a customer's KYC
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyKyc(@PathVariable String customerId,
                                                 @Valid @RequestBody KycVerifyRequestDto request) {
        KycResponseDto response = kycService.verifyKyc(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer KYC verified successfully.", response));
    }

    // Retrieves a customer's KYC status
    @GetMapping
    public ResponseEntity<ApiResponse> getKycStatus(@PathVariable String customerId) {
        KycResponseDto response = kycService.getKycStatus(customerId);
        return ResponseEntity.ok(ApiResponse.success("KYC status retrieved successfully.", response));
    }

    // Rejects a customer's KYC
    @PutMapping("/reject")
    public ResponseEntity<ApiResponse> rejectKyc(@PathVariable String customerId,
                                                 @Valid @RequestBody KycRejectRequestDto request) {
        KycResponseDto response = kycService.rejectKyc(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Customer KYC rejected successfully.", response));
    }

    // Resubmits a customer's KYC after rejection
    @PostMapping("/resubmit")
    public ResponseEntity<ApiResponse> resubmitKyc(@PathVariable String customerId,
                                                   @RequestBody KycResubmitRequestDto request) {
        KycResponseDto response = kycService.resubmitKyc(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("KYC resubmitted successfully.", response));
    }

}