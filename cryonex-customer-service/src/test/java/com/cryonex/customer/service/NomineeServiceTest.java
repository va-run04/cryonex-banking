package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.NomineeRequestDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerKyc;
import com.cryonex.customer.entity.CustomerNominee;
import com.cryonex.customer.enums.KycStatus;
import com.cryonex.customer.enums.Relationship;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerKycRepository;
import com.cryonex.customer.repository.CustomerNomineeRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NomineeServiceTest {

    @Mock
    private CustomerNomineeRepository nomineeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerKycRepository kycRepository;

    @Mock
    private CustomerAuditRepository customerAuditRepository;

    @Mock
    private IdGeneratorUtil idGeneratorUtil;

    @InjectMocks
    private NomineeService nomineeService;

    @Test
    void addNominee_shouldThrowException_whenTotalSharePercentageExceeds100() {

        Customer customer = new Customer();
        customer.setCustomerId("CUS100001");

        CustomerKyc kyc = new CustomerKyc();
        kyc.setKycStatus(KycStatus.VERIFIED);

        CustomerNominee existingNominee = new CustomerNominee();
        existingNominee.setSharePercentage(new BigDecimal("70"));

        NomineeRequestDto request = new NomineeRequestDto();
        request.setNomineeName("Test Nominee");
        request.setRelationship(Relationship.SPOUSE);
        request.setDob(LocalDate.of(1990, 1, 1));
        request.setMobile("9876543210");
        request.setSharePercentage(new BigDecimal("50"));

        when(customerRepository.findById("CUS100001")).thenReturn(Optional.of(customer));
        when(kycRepository.findByCustomer(customer)).thenReturn(Optional.of(kyc));
        when(nomineeRepository.findByCustomer(customer)).thenReturn(List.of(existingNominee));

        assertThrows(BusinessValidationException.class, () -> {
            nomineeService.addNominee("CUS100001", request);
        });
    }

}