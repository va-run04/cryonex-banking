package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.response.AuditResponseDto;
import com.cryonex.customer.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @PreAuthorize("hasRole('OPERATIONS_EXECUTIVE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAuditHistory(@PathVariable String customerId) {
        List<AuditResponseDto> response = auditService.getAuditHistory(customerId);
        return ResponseEntity.ok(ApiResponse.success("Audit history retrieved successfully.", response));
    }

    @GetMapping("/{auditId}")
    @PreAuthorize("hasRole('OPERATIONS_EXECUTIVE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAuditRecord(@PathVariable String customerId,
                                                      @PathVariable String auditId) {
        AuditResponseDto response = auditService.getAuditRecord(customerId, auditId);
        return ResponseEntity.ok(ApiResponse.success("Audit record retrieved successfully.", response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('OPERATIONS_EXECUTIVE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> searchAudit(@PathVariable String customerId,
                                                   @RequestParam(required = false) String action,
                                                   @RequestParam(required = false) String performedBy,
                                                   @RequestParam(required = false) LocalDateTime fromDate,
                                                   @RequestParam(required = false) LocalDateTime toDate,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AuditResponseDto> response = auditService.searchAudit(customerId, action, performedBy, fromDate, toDate, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit search completed successfully.", response));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('OPERATIONS_EXECUTIVE') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportAuditReport(@PathVariable String customerId,
                                                    @RequestParam String format) {

        byte[] fileContent = auditService.exportAuditReport(customerId, format);

        String fileName = "audit_report_" + customerId + ("PDF".equalsIgnoreCase(format) ? ".pdf" : ".xlsx");
        MediaType mediaType = "PDF".equalsIgnoreCase(format)
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(mediaType)
                .body(fileContent);
    }

}