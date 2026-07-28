package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.KycRejectRequestDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerKyc;
import com.cryonex.customer.enums.KycStatus;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerDocumentRepository;
import com.cryonex.customer.repository.CustomerKycRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KycServiceTest {

    @Mock
    private CustomerKycRepository kycRepository;

    @Mock
    private CustomerDocumentRepository documentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAuditRepository customerAuditRepository;

    @Mock
    private IdGeneratorUtil idGeneratorUtil;

    @InjectMocks
    private KycService kycService;

    @Test
    void rejectKyc_shouldThrowException_whenKycIsAlreadyVerified() {

        Customer customer = new Customer();
        customer.setCustomerId("CUS100001");

        CustomerKyc kyc = new CustomerKyc();
        kyc.setKycStatus(KycStatus.VERIFIED);

        KycRejectRequestDto request = new KycRejectRequestDto();
        request.setReason("Test rejection");
        request.setVerifiedBy("EMP10001");

        when(customerRepository.findById("CUS100001")).thenReturn(Optional.of(customer));
        when(kycRepository.findByCustomer(customer)).thenReturn(Optional.of(kyc));

        assertThrows(BusinessValidationException.class, () -> {
            kycService.rejectKyc("CUS100001", request);
        });
    }

}