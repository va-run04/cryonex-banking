package com.cryonex.customer.service;


import com.cryonex.customer.dto.request.CustomerRequestDto;
import com.cryonex.customer.dto.request.CustomerStatusUpdateRequestDto;
import com.cryonex.customer.dto.request.CustomerUpdateRequestDto;
import com.cryonex.customer.dto.response.CustomerCreateResponseDto;
import com.cryonex.customer.dto.response.CustomerResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.enums.CustomerStatus;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.DuplicateResourceException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public CustomerService(CustomerRepository customerRepository, CustomerAuditRepository customerAuditRepository, IdGeneratorUtil idGeneratorUtil){
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }


    // CREATE CUSTOMER
    public CustomerCreateResponseDto createCustomer(CustomerRequestDto request){
        if(customerRepository.existsByPanNumber(request.getPanNumber())){
            throw new DuplicateResourceException("CUS_101", "PAN Number already exists.");
        }

        if(customerRepository.existsByAadhaarNumber(request.getAadhaarNumber())){
            throw new DuplicateResourceException("CUS_102", "Aadhaar Number already exists.");
        }
        int age = Period.between(request.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new BusinessValidationException("CUS_103", "Customer age should be 18 years or above.");
        }
        long nextSequence = 100001 + customerRepository.count();
        String customerId = idGeneratorUtil.generateId("CUS", nextSequence);
        String cifNumber = idGeneratorUtil.generateId("CIF", nextSequence);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCifNumber(cifNumber);
        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setOccupation(request.getOccupation());
        customer.setAnnualIncome(request.getAnnualIncome());
        customer.setCustomerType(request.getCustomerType());
        customer.setCustomerCategory(request.getCustomerCategory());
        customer.setPanNumber(request.getPanNumber());
        customer.setAadhaarNumber(request.getAadhaarNumber());
        customer.setNationality(request.getNationality());
        customer.setCustomerStatus(CustomerStatus.ACTIVE);

        Customer savedCustomer = customerRepository.save(customer);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", 100000 + customerAuditRepository.count()));
        audit.setCustomer(savedCustomer);
        audit.setAction("CUSTOMER_CREATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Customer onboarded with status ACTIVE");
        customerAuditRepository.save(audit);

        CustomerCreateResponseDto response = new CustomerCreateResponseDto();
        response.setCustomerId(savedCustomer.getCustomerId());
        response.setCifNumber(savedCustomer.getCifNumber());
        response.setCreatedDate(savedCustomer.getCreatedDate());

        return response;
    }

    // 2) GET CUSTOMER DETAILS
    public CustomerResponseDto getCustomerDetails(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CUS_404", "Customer not found."));

        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(customer.getCustomerId());
        response.setCifNumber(customer.getCifNumber());

        String fullName = customer.getFirstName() +
                (customer.getMiddleName() != null ? " " + customer.getMiddleName() : "") +
                " " + customer.getLastName();
        response.setCustomerName(fullName);

        response.setStatus(customer.getCustomerStatus());

        return response;
    }

    // 3) UPDATE CUSTOMER
    public CustomerResponseDto updateCustomer(String customerId, CustomerUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CUS_404", "Customer not found."));

        if (request.getFirstName() != null) {
            customer.setFirstName(request.getFirstName());
        }
        if (request.getMiddleName() != null) {
            customer.setMiddleName(request.getMiddleName());
        }
        if (request.getLastName() != null) {
            customer.setLastName(request.getLastName());
        }
        if (request.getMaritalStatus() != null) {
            customer.setMaritalStatus(request.getMaritalStatus());
        }
        if (request.getOccupation() != null) {
            customer.setOccupation(request.getOccupation());
        }
        if (request.getAnnualIncome() != null) {
            customer.setAnnualIncome(request.getAnnualIncome());
        }
        if (request.getNationality() != null) {
            customer.setNationality(request.getNationality());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", 100000 + customerAuditRepository.count()));
        audit.setCustomer(updatedCustomer);
        audit.setAction("CUSTOMER_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Customer details updated");
        customerAuditRepository.save(audit);

        CustomerResponseDto response = new CustomerResponseDto();
        response.setCustomerId(updatedCustomer.getCustomerId());
        response.setCifNumber(updatedCustomer.getCifNumber());

        String fullName = updatedCustomer.getFirstName() +
                (updatedCustomer.getMiddleName() != null ? " " + updatedCustomer.getMiddleName() : "") +
                " " + updatedCustomer.getLastName();
        response.setCustomerName(fullName);

        response.setStatus(updatedCustomer.getCustomerStatus());

        return response;
    }

    //4) UPDATE CUSTOMER STATUS
    public void updateCustomerStatus(String customerId, CustomerStatusUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CUS_404", "Customer not found."));

        CustomerStatus oldStatus = customer.getCustomerStatus();
        customer.setCustomerStatus(request.getStatus());

        customerRepository.save(customer);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", 100000 + customerAuditRepository.count()));
        audit.setCustomer(customer);
        audit.setAction("CUSTOMER_STATUS_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue(oldStatus.toString());
        audit.setNewValue(request.getStatus().toString());
        customerAuditRepository.save(audit);

    }

    //5) DELETE CUSTOMER
    public void deleteCustomer(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CUS_404", "Customer not found."));

        CustomerStatus oldStatus = customer.getCustomerStatus();
        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        customerRepository.save(customer);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", 100000 + customerAuditRepository.count()));
        audit.setCustomer(customer);
        audit.setAction("CUSTOMER_DEACTIVATED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue(oldStatus.toString());
        audit.setNewValue(CustomerStatus.INACTIVE.toString());
        customerAuditRepository.save(audit);
    }
}
