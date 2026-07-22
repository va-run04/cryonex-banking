package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.DocumentRequestDto;
import com.cryonex.customer.dto.response.DocumentResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.entity.CustomerDocument;
import com.cryonex.customer.entity.CustomerKyc;
import com.cryonex.customer.enums.KycStatus;
import com.cryonex.customer.enums.VerificationStatus;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerDocumentRepository;
import com.cryonex.customer.repository.CustomerKycRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final CustomerDocumentRepository documentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final CustomerKycRepository kycRepository;
    private final IdGeneratorUtil idGeneratorUtil;
    private final StorageService storageService;

    public DocumentService(CustomerDocumentRepository documentRepository,
                           CustomerRepository customerRepository,
                           CustomerAuditRepository customerAuditRepository,
                           CustomerKycRepository kycRepository,
                           IdGeneratorUtil idGeneratorUtil,
                           StorageService storageService) {
        this.documentRepository = documentRepository;
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.kycRepository = kycRepository;
        this.idGeneratorUtil = idGeneratorUtil;
        this.storageService = storageService;
    }

    // 1) UPLOAD DOCUMENT
    @Transactional
    public DocumentResponseDto uploadDocument(String customerId, DocumentRequestDto request, MultipartFile file){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_001", "Customer not found."));

        if (documentRepository.findByCustomerAndDocumentType(customer, request.getDocumentType()).isPresent()) {
            throw new BusinessValidationException("DOC_001", "This document type has already been uploaded.");
        }

        String contentType = file.getContentType();

        if(contentType == null || !(contentType.equals("application/pdf") || contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            throw new BusinessValidationException("DOC_001", "Unsupported document type. Allowed: PDF, JPEG, PNG");
        }

        if(file.getSize() > 5 * 1024 * 1024){
            throw new BusinessValidationException("DOC_001", "File size exceeds 5 MB Limit.");
        }

        String filePath = storageService.saveFile(customerId, file);

        String documentId = idGeneratorUtil.generateId("DOC", "DOCUMENT");
        CustomerDocument document = new CustomerDocument();
        document.setDocumentId(documentId);
        document.setCustomer(customer);
        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setDocumentPath(filePath);
        document.setVerificationStatus(VerificationStatus.PENDING);
        document.setUploadedDate(LocalDateTime.now());

        CustomerDocument savedDocument = documentRepository.save(document);

        // Ensure a PENDING KYC record exists once any document is uploaded
        CustomerKyc existingKyc = kycRepository.findByCustomer(customer).orElse(null);
        if (existingKyc == null) {
            CustomerKyc newKyc = new CustomerKyc();
            newKyc.setKycId(idGeneratorUtil.generateId("KYC", "KYC"));
            newKyc.setCustomer(customer);
            newKyc.setPanVerified(false);
            newKyc.setAadhaarVerified(false);
            newKyc.setKycStatus(KycStatus.PENDING);
            kycRepository.save(newKyc);
        }

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("DOCUMENT_UPLOADED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Document uploaded: " + request.getDocumentType());
        customerAuditRepository.save(audit);

        DocumentResponseDto response = new DocumentResponseDto();
        response.setDocumentId(savedDocument.getDocumentId());
        response.setDocumentType(savedDocument.getDocumentType());
        response.setDocumentNumber(savedDocument.getDocumentNumber());
        response.setVerificationStatus(savedDocument.getVerificationStatus());
        response.setUploadedDate(savedDocument.getUploadedDate());

        return response;
    }

    // 2) GET CUSTOMER DOCUMENTS
    public List<DocumentResponseDto> getDocuments(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_001", "Customer not found."));

        List<CustomerDocument> documents = documentRepository.findByCustomer(customer);

        List<DocumentResponseDto> responseList = new ArrayList<>();

        for (CustomerDocument document : documents) {
            DocumentResponseDto dto = new DocumentResponseDto();
            dto.setDocumentId(document.getDocumentId());
            dto.setDocumentType(document.getDocumentType());
            dto.setDocumentNumber(document.getDocumentNumber());
            dto.setVerificationStatus(document.getVerificationStatus());
            dto.setUploadedDate(document.getUploadedDate());
            responseList.add(dto);
        }

        return responseList;
    }

    // 3) DOWNLOAD DOCUMENT

    public CustomerDocument getDocumentForDownload(String customerId, String documentId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_001", "Customer not found."));

        CustomerDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_002", "Document not found."));

        return document;
    }

    public byte[] readDocumentFile(String filePath) {
        return storageService.readFile(filePath);
    }

    // 4) UPDATE DOCUMENT (RE-UPLOAD)
    @Transactional
    public DocumentResponseDto updateDocument(String customerId, String documentId, String documentNumber, MultipartFile file) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_001", "Customer not found."));

        CustomerDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_002", "Document does not exist."));

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessValidationException("DOC_001", "File size exceeds 5 MB limit.");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png"))) {
            throw new BusinessValidationException("DOC_001", "Unsupported document type. Allowed: PDF, JPEG, PNG.");
        }

        storageService.deleteFile(document.getDocumentPath());

        String newFilePath = storageService.saveFile(customerId, file);

        if (documentNumber != null) {
            document.setDocumentNumber(documentNumber);
        }
        document.setDocumentPath(newFilePath);
        document.setVerificationStatus(VerificationStatus.PENDING);
        document.setUploadedDate(LocalDateTime.now());

        CustomerDocument updatedDocument = documentRepository.save(document);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("DOCUMENT_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Document re-uploaded, verification reset to PENDING");
        customerAuditRepository.save(audit);

        DocumentResponseDto response = new DocumentResponseDto();
        response.setDocumentId(updatedDocument.getDocumentId());
        response.setDocumentType(updatedDocument.getDocumentType());
        response.setDocumentNumber(updatedDocument.getDocumentNumber());
        response.setVerificationStatus(updatedDocument.getVerificationStatus());
        response.setUploadedDate(updatedDocument.getUploadedDate());

        return response;
    }

    // 5) DELETE DOCUMENT
    @Transactional
    public void deleteDocument(String customerId, String documentId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_001", "Customer not found."));

        CustomerDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("DOC_002", "Document does not exist."));

        if (document.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new BusinessValidationException("DOC_003", "Verified document cannot be deleted.");
        }

        storageService.deleteFile(document.getDocumentPath());

        documentRepository.delete(document);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("DOCUMENT_DELETED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue("Document deleted: " + documentId);
        customerAuditRepository.save(audit);

    }
}
