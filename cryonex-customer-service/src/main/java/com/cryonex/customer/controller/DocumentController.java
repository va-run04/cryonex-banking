package com.cryonex.customer.controller;

import com.cryonex.customer.dto.ApiResponse;
import com.cryonex.customer.dto.request.DocumentRequestDto;
import com.cryonex.customer.dto.response.DocumentResponseDto;
import com.cryonex.customer.entity.CustomerDocument;
import com.cryonex.customer.service.DocumentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // Uploads a new document for the customer
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadDocument(@PathVariable String customerId,
                                                      @RequestParam("documentType") String documentType,
                                                      @RequestParam("documentNumber") String documentNumber,
                                                      @RequestParam("file") MultipartFile file) {

        DocumentRequestDto request = new DocumentRequestDto();
        request.setDocumentType(com.cryonex.customer.enums.DocumentType.valueOf(documentType));
        request.setDocumentNumber(documentNumber);

        DocumentResponseDto response = documentService.uploadDocument(customerId, request, file);
        return new ResponseEntity<>(
                ApiResponse.success("Document uploaded successfully.", response),
                HttpStatus.CREATED
        );
    }

    // Retrieves metadata for all documents belonging to the customer
    @GetMapping
    public ResponseEntity<ApiResponse> getDocuments(@PathVariable String customerId) {
        List<DocumentResponseDto> response = documentService.getDocuments(customerId);
        return ResponseEntity.ok(ApiResponse.success("Documents retrieved successfully.", response));
    }

    // Downloads the actual document file
    @GetMapping("/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String customerId,
                                                   @PathVariable String documentId) {

        CustomerDocument document = documentService.getDocumentForDownload(customerId, documentId);
        byte[] fileContent = documentService.readDocumentFile(document.getDocumentPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentId() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }

    // Updates (re-uploads) an existing document
    @PutMapping(value = "/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateDocument(@PathVariable String customerId,
                                                      @PathVariable String documentId,
                                                      @RequestParam(value = "documentNumber", required = false) String documentNumber,
                                                      @RequestParam("file") MultipartFile file) {
        DocumentResponseDto response = documentService.updateDocument(customerId, documentId, documentNumber, file);
        return ResponseEntity.ok(ApiResponse.success("Document updated successfully.", response));
    }

    // Deletes a document
    @DeleteMapping("/{documentId}")
    public ResponseEntity<ApiResponse> deleteDocument(@PathVariable String customerId,
                                                      @PathVariable String documentId) {
        documentService.deleteDocument(customerId, documentId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully.", null));
    }

}