package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.NomineeRequestDto;
import com.cryonex.customer.dto.request.NomineeUpdateRequestDto;
import com.cryonex.customer.dto.response.NomineeResponseDto;
import com.cryonex.customer.service.NomineeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/nominees")
public class NomineeController {

    private final NomineeService nomineeService;

    public NomineeController(NomineeService nomineeService) {
        this.nomineeService = nomineeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addNominee(@PathVariable String customerId,
                                                  @Valid @RequestBody NomineeRequestDto request) {
        NomineeResponseDto response = nomineeService.addNominee(customerId, request);
        return new ResponseEntity<>(
                ApiResponse.success("Nominee added successfully.", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getNominees(@PathVariable String customerId) {
        List<NomineeResponseDto> response = nomineeService.getNominees(customerId);
        return ResponseEntity.ok(ApiResponse.success("Nominees retrieved successfully.", response));
    }

    @PutMapping("/{nomineeId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateNominee(@PathVariable String customerId,
                                                     @PathVariable String nomineeId,
                                                     @RequestBody NomineeUpdateRequestDto request) {
        NomineeResponseDto response = nomineeService.updateNominee(customerId, nomineeId, request);
        return ResponseEntity.ok(ApiResponse.success("Nominee updated successfully.", response));
    }

    @DeleteMapping("/{nomineeId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteNominee(@PathVariable String customerId,
                                                     @PathVariable String nomineeId) {
        nomineeService.deleteNominee(customerId, nomineeId);
        return ResponseEntity.ok(ApiResponse.success("Nominee deleted successfully.", null));
    }

    @PutMapping("/{nomineeId}/verify")
    @PreAuthorize("hasRole('KYC_OFFICER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> verifyNominee(@PathVariable String customerId,
                                                     @PathVariable String nomineeId) {
        NomineeResponseDto response = nomineeService.verifyNominee(customerId, nomineeId);
        return ResponseEntity.ok(ApiResponse.success("Nominee verified successfully.", response));
    }

}