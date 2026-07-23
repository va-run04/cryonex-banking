package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.NomineeRequestDto;
import com.cryonex.customer.dto.request.NomineeUpdateRequestDto;
import com.cryonex.customer.dto.response.NomineeResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.entity.CustomerKyc;
import com.cryonex.customer.entity.CustomerNominee;
import com.cryonex.customer.enums.KycStatus;
import com.cryonex.customer.enums.VerificationStatus;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerKycRepository;
import com.cryonex.customer.repository.CustomerNomineeRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class NomineeService {

    private final CustomerNomineeRepository nomineeRepository;
    private final CustomerRepository customerRepository;
    private final CustomerKycRepository kycRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public NomineeService(CustomerNomineeRepository nomineeRepository,
                          CustomerRepository customerRepository,
                          CustomerKycRepository kycRepository,
                          CustomerAuditRepository customerAuditRepository,
                          IdGeneratorUtil idGeneratorUtil) {
        this.nomineeRepository = nomineeRepository;
        this.customerRepository = customerRepository;
        this.kycRepository = kycRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }


    // 1) ADD NOMINEE
    @Transactional
    public NomineeResponseDto addNominee(String customerId, NomineeRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_002", "Customer KYC record not found."));

        if (kyc.getKycStatus() != KycStatus.VERIFIED) {
            throw new BusinessValidationException("NOM_003", "Customer KYC must be verified before adding a nominee.");
        }

        List<CustomerNominee> existingNominees = nomineeRepository.findByCustomer(customer);

        BigDecimal totalExistingShare = BigDecimal.ZERO;
        for (CustomerNominee nominee : existingNominees) {
            totalExistingShare = totalExistingShare.add(nominee.getSharePercentage());
        }

        BigDecimal totalAfterAdding = totalExistingShare.add(request.getSharePercentage());

        if (totalAfterAdding.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessValidationException("NOM_004", "Total nominee share percentage cannot exceed 100.");
        }

        String nomineeId = idGeneratorUtil.generateId("NOM", "NOMINEE");

        CustomerNominee nominee = new CustomerNominee();
        nominee.setNomineeId(nomineeId);
        nominee.setCustomer(customer);
        nominee.setNomineeName(request.getNomineeName());
        nominee.setRelationship(request.getRelationship());
        nominee.setDob(request.getDob());
        nominee.setMobile(request.getMobile());
        nominee.setSharePercentage(request.getSharePercentage());
        nominee.setVerificationStatus(VerificationStatus.PENDING);

        CustomerNominee savedNominee = nomineeRepository.save(nominee);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("NOMINEE_ADDED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Nominee added: " + request.getNomineeName());
        customerAuditRepository.save(audit);

        NomineeResponseDto response = new NomineeResponseDto();
        response.setNomineeId(savedNominee.getNomineeId());
        response.setNomineeName(savedNominee.getNomineeName());
        response.setRelationship(savedNominee.getRelationship());
        response.setDob(savedNominee.getDob());
        response.setMobile(savedNominee.getMobile());
        response.setSharePercentage(savedNominee.getSharePercentage());
        response.setVerificationStatus(savedNominee.getVerificationStatus());

        return response;
    }


    //Get Nominee
    public List<NomineeResponseDto> getNominees(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_001", "Customer not found."));

        List<CustomerNominee> nominees = nomineeRepository.findByCustomer(customer);

        List<NomineeResponseDto> responseList = new ArrayList<>();

        for (CustomerNominee nominee : nominees) {
            NomineeResponseDto dto = new NomineeResponseDto();
            dto.setNomineeId(nominee.getNomineeId());
            dto.setNomineeName(nominee.getNomineeName());
            dto.setRelationship(nominee.getRelationship());
            dto.setDob(nominee.getDob());
            dto.setMobile(nominee.getMobile());
            dto.setSharePercentage(nominee.getSharePercentage());
            dto.setVerificationStatus(nominee.getVerificationStatus());
            responseList.add(dto);
        }

        return responseList;
    }


    // 3) UPDATE NOMINEE
    @Transactional
    public NomineeResponseDto updateNominee(String customerId, String nomineeId, NomineeUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_001", "Customer not found."));

        CustomerNominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_005", "Nominee not found."));

        if (request.getSharePercentage() != null) {
            List<CustomerNominee> existingNominees = nomineeRepository.findByCustomer(customer);

            BigDecimal totalOtherShares = BigDecimal.ZERO;
            for (CustomerNominee existing : existingNominees) {
                if (!existing.getNomineeId().equals(nomineeId)) {
                    totalOtherShares = totalOtherShares.add(existing.getSharePercentage());
                }
            }

            BigDecimal totalAfterUpdate = totalOtherShares.add(request.getSharePercentage());

            if (totalAfterUpdate.compareTo(new BigDecimal("100")) > 0) {
                throw new BusinessValidationException("NOM_004", "Total nominee share percentage cannot exceed 100.");
            }
        }

        if (request.getNomineeName() != null) {
            nominee.setNomineeName(request.getNomineeName());
        }
        if (request.getRelationship() != null) {
            nominee.setRelationship(request.getRelationship());
        }
        if (request.getDob() != null) {
            nominee.setDob(request.getDob());
        }
        if (request.getMobile() != null) {
            nominee.setMobile(request.getMobile());
        }
        if (request.getSharePercentage() != null) {
            nominee.setSharePercentage(request.getSharePercentage());
        }

        CustomerNominee updatedNominee = nomineeRepository.save(nominee);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("NOMINEE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Nominee updated: " + nomineeId);
        customerAuditRepository.save(audit);

        NomineeResponseDto response = new NomineeResponseDto();
        response.setNomineeId(updatedNominee.getNomineeId());
        response.setNomineeName(updatedNominee.getNomineeName());
        response.setRelationship(updatedNominee.getRelationship());
        response.setDob(updatedNominee.getDob());
        response.setMobile(updatedNominee.getMobile());
        response.setSharePercentage(updatedNominee.getSharePercentage());
        response.setVerificationStatus(updatedNominee.getVerificationStatus());

        return response;
    }


    // 4) DELETE NOMINEE
    @Transactional
    public void deleteNominee(String customerId, String nomineeId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_001", "Customer not found."));

        CustomerNominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_005", "Nominee not found."));

        if (nominee.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new BusinessValidationException("NOM_006", "Verified nominee cannot be deleted without supervisor approval.");
        }

        nomineeRepository.delete(nominee);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("NOMINEE_DELETED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue("Nominee deleted: " + nomineeId);
        customerAuditRepository.save(audit);

    }

    // 5)  VERIFY NOMINEE
    @Transactional
    public NomineeResponseDto verifyNominee(String customerId, String nomineeId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_001", "Customer not found."));

        CustomerKyc kyc = kycRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_002", "Customer KYC record not found."));

        if (kyc.getKycStatus() != KycStatus.VERIFIED) {
            throw new BusinessValidationException("NOM_003", "Customer KYC must be verified before verifying a nominee.");
        }

        CustomerNominee nominee = nomineeRepository.findById(nomineeId)
                .orElseThrow(() -> new ResourceNotFoundException("NOM_005", "Nominee not found."));

        nominee.setVerificationStatus(VerificationStatus.VERIFIED);

        CustomerNominee updatedNominee = nomineeRepository.save(nominee);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("NOMINEE_VERIFIED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Nominee verified: " + nomineeId);
        customerAuditRepository.save(audit);

        NomineeResponseDto response = new NomineeResponseDto();
        response.setNomineeId(updatedNominee.getNomineeId());
        response.setNomineeName(updatedNominee.getNomineeName());
        response.setRelationship(updatedNominee.getRelationship());
        response.setDob(updatedNominee.getDob());
        response.setMobile(updatedNominee.getMobile());
        response.setSharePercentage(updatedNominee.getSharePercentage());
        response.setVerificationStatus(updatedNominee.getVerificationStatus());

        return response;
    }
}