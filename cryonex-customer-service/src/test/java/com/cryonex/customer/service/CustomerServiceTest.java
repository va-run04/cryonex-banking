package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.CustomerRequestDto;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAuditRepository customerAuditRepository;

    @Mock
    private IdGeneratorUtil idGeneratorUtil;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomer_shouldThrowException_whenAgeIsUnder18() {

        CustomerRequestDto request = new CustomerRequestDto();
        request.setFirstName("Test");
        request.setLastName("User");
        request.setDateOfBirth(LocalDate.now().minusYears(10));
        request.setPanNumber("ABCDE1234F");
        request.setAadhaarNumber("123456789012");

        when(customerRepository.existsByPanNumber("ABCDE1234F")).thenReturn(false);
        when(customerRepository.existsByAadhaarNumber("123456789012")).thenReturn(false);

        assertThrows(BusinessValidationException.class, () -> {
            customerService.createCustomer(request);
        });
    }

}