package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.KycRejectRequestDto;
import com.cryonex.customer.dto.request.KycResubmitRequestDto;
import com.cryonex.customer.dto.request.KycVerifyRequestDto;
import com.cryonex.customer.dto.response.KycResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.entity.CustomerDocument;
import com.cryonex.customer.entity.CustomerKyc;
import com.cryonex.customer.enums.DocumentType;
import com.cryonex.customer.enums.KycStatus;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerDocumentRepository;
import com.cryonex.customer.repository.CustomerKycRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KycService {

    private final CustomerKycRepository kycRepository;
    private final CustomerDocumentRepository documentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public KycService(CustomerKycRepository kycRepository,
                      CustomerDocumentRepository documentRepository,
                      CustomerRepository customerRepository,
                      CustomerAuditRepository customerAuditRepository,
                      IdGeneratorUtil idGeneratorUtil) {
        this.kycRepository = kycRepository;
        this.documentRepository = documentRepository;
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    // 1) Verify KyC
    public KycResponseDto verifyKyc(String customerId, KycVerifyRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_003", "KYC record not found. Please upload documents first."));

        if (kyc.getKycStatus() == KycStatus.REJECTED) {
            throw new BusinessValidationException("KYC_006", "Cannot verify a rejected KYC. Please resubmit first.");
        }

        boolean panUploaded = documentRepository.findByCustomerAndDocumentType(customer, DocumentType.PAN).isPresent();
        if (!panUploaded) {
            throw new BusinessValidationException("KYC_001", "PAN document not uploaded.");
        }

        boolean aadhaarUploaded = documentRepository.findByCustomerAndDocumentType(customer, DocumentType.AADHAAR).isPresent();
        if (!aadhaarUploaded) {
            throw new BusinessValidationException("KYC_002", "Aadhaar document not uploaded.");
        }

        kyc.setPanVerified(request.getPanVerified());
        kyc.setAadhaarVerified(request.getAadhaarVerified());
        kyc.setKycStatus(KycStatus.VERIFIED);
        kyc.setVerifiedBy(request.getVerifiedBy());
        kyc.setVerifiedDate(LocalDateTime.now());

        CustomerKyc savedKyc = kycRepository.save(kyc);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("KYC_VERIFIED");
        audit.setPerformedBy(request.getVerifiedBy());
        audit.setNewValue("KYC status: VERIFIED");
        customerAuditRepository.save(audit);

        KycResponseDto response = new KycResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setPanVerified(savedKyc.getPanVerified());
        response.setAadhaarVerified(savedKyc.getAadhaarVerified());
        response.setKycStatus(savedKyc.getKycStatus());
        response.setVerifiedBy(savedKyc.getVerifiedBy());
        response.setVerifiedDate(savedKyc.getVerifiedDate());

        return response;
    }

    // 2) Get KyC STATUS
    public KycResponseDto getKycStatus(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_003", "KYC record not found."));

        KycResponseDto response = new KycResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setPanVerified(kyc.getPanVerified());
        response.setAadhaarVerified(kyc.getAadhaarVerified());
        response.setKycStatus(kyc.getKycStatus());
        response.setVerifiedBy(kyc.getVerifiedBy());
        response.setVerifiedDate(kyc.getVerifiedDate());

        return response;
    }

    // 3) REJECT KYC
    @Transactional
    public KycResponseDto rejectKyc(String customerId, KycRejectRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_003", "KYC record not found."));

        if (kyc.getKycStatus() != KycStatus.PENDING) {
            throw new BusinessValidationException("KYC_004", "Customer KYC is already verified.");
        }

        kyc.setKycStatus(KycStatus.REJECTED);
        kyc.setRejectionReason(request.getReason());
        kyc.setVerifiedBy(request.getVerifiedBy());
        kyc.setVerifiedDate(LocalDateTime.now());

        CustomerKyc updatedKyc = kycRepository.save(kyc);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("KYC_REJECTED");
        audit.setPerformedBy(request.getVerifiedBy());
        audit.setNewValue("KYC status: REJECTED, reason: " + request.getReason());
        customerAuditRepository.save(audit);

        KycResponseDto response = new KycResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setPanVerified(updatedKyc.getPanVerified());
        response.setAadhaarVerified(updatedKyc.getAadhaarVerified());
        response.setKycStatus(updatedKyc.getKycStatus());
        response.setVerifiedBy(updatedKyc.getVerifiedBy());
        response.setVerifiedDate(updatedKyc.getVerifiedDate());

        return response;
    }


    // 4) RESUBMIT KYC
    @Transactional
    public KycResponseDto resubmitKyc(String customerId, KycResubmitRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("KYC_003", "KYC record not found."));

        if (kyc.getKycStatus() != KycStatus.REJECTED) {
            throw new BusinessValidationException("KYC_005", "Customer KYC is already verified.");
        }

        kyc.setKycStatus(KycStatus.PENDING);
        kyc.setRejectionReason(null);

        CustomerKyc updatedKyc = kycRepository.save(kyc);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("KYC_RESUBMITTED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("KYC status: PENDING, remarks: " + request.getRemarks());
        customerAuditRepository.save(audit);

        KycResponseDto response = new KycResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setPanVerified(updatedKyc.getPanVerified());
        response.setAadhaarVerified(updatedKyc.getAadhaarVerified());
        response.setKycStatus(updatedKyc.getKycStatus());
        response.setVerifiedBy(updatedKyc.getVerifiedBy());
        response.setVerifiedDate(updatedKyc.getVerifiedDate());

        return response;
    }



}