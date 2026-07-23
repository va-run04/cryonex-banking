package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.MarketingUpdateRequestDto;
import com.cryonex.customer.dto.request.NotificationUpdateRequestDto;
import com.cryonex.customer.dto.request.PreferenceRequestDto;
import com.cryonex.customer.dto.request.PreferenceUpdateRequestDto;
import com.cryonex.customer.dto.response.PreferenceResponseDto;
import com.cryonex.customer.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    // Creates preferences for a customer
    @PostMapping
    public ResponseEntity<ApiResponse> createPreference(@PathVariable String customerId,
                                                        @Valid @RequestBody PreferenceRequestDto request) {
        PreferenceResponseDto response = preferenceService.createPreference(customerId, request);
        return new ResponseEntity<>(
                ApiResponse.success("Preferences created successfully.", response),
                HttpStatus.CREATED
        );
    }

    // Retrieves a customer's preferences
    @GetMapping
    public ResponseEntity<ApiResponse> getPreferences(@PathVariable String customerId) {
        PreferenceResponseDto response = preferenceService.getPreferences(customerId);
        return ResponseEntity.ok(ApiResponse.success("Preferences retrieved successfully.", response));
    }

    // Updates all preference fields
    @PutMapping
    public ResponseEntity<ApiResponse> updatePreferences(@PathVariable String customerId,
                                                         @RequestBody PreferenceUpdateRequestDto request) {
        PreferenceResponseDto response = preferenceService.updatePreferences(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Preferences updated successfully.", response));
    }

    // Updates notification (email/SMS) preferences
    @PutMapping("/notifications")
    public ResponseEntity<ApiResponse> updateNotificationPreferences(@PathVariable String customerId,
                                                                     @RequestBody NotificationUpdateRequestDto request) {
        PreferenceResponseDto response = preferenceService.updateNotificationPreferences(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Notification preferences updated successfully.", response));
    }

    // Updates marketing consent
    @PutMapping("/marketing")
    public ResponseEntity<ApiResponse> updateMarketingPreference(@PathVariable String customerId,
                                                                 @Valid @RequestBody MarketingUpdateRequestDto request) {
        PreferenceResponseDto response = preferenceService.updateMarketingPreference(customerId, request);
        return ResponseEntity.ok(ApiResponse.success("Marketing preference updated successfully.", response));
    }

}