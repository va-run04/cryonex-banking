package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.AddressRequestDto;
import com.cryonex.customer.dto.request.AddressUpdateRequestDto;
import com.cryonex.customer.dto.response.AddressResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAddress;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAddressRepository;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final CustomerAddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public AddressService(CustomerAddressRepository addressRepository,
                          CustomerRepository customerRepository,
                          CustomerAuditRepository customerAuditRepository,
                          IdGeneratorUtil idGeneratorUtil) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }

    // 1) ADD ADDRESS
    public AddressResponseDto addAddress(String customerId, AddressRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_001", "Customer not found."));

        if (request.isPrimary()) {
            List<CustomerAddress> existingAddresses = addressRepository.findByCustomer(customer);
            for (CustomerAddress existing : existingAddresses) {
                if (existing.getIsPrimary()) {
                    existing.setIsPrimary(false);
                    addressRepository.save(existing);
                }
            }
        }

        long nextSequence = 100001 + addressRepository.count();
        String addressId = idGeneratorUtil.generateId("ADDR", nextSequence);

        CustomerAddress address = new CustomerAddress();
        address.setAddressId(addressId);
        address.setCustomer(customer);
        address.setAddressType(request.getAddressType());
        address.setDoorNumber(request.getDoorNumber());
        address.setStreet(request.getStreet());
        address.setArea(request.getArea());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setIsPrimary(request.isPrimary());

        CustomerAddress savedAddress = addressRepository.save(address);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", 100000 + customerAuditRepository.count()));
        audit.setCustomer(customer);
        audit.setAction("ADDRESS_ADDED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Address added: " + request.getAddressType());
        customerAuditRepository.save(audit);

        return mapToResponseDto(savedAddress);
    }

    private AddressResponseDto mapToResponseDto(CustomerAddress address) {
        AddressResponseDto dto = new AddressResponseDto();
        dto.setAddressId(address.getAddressId());
        dto.setAddressType(address.getAddressType());
        dto.setDoorNumber(address.getDoorNumber());
        dto.setStreet(address.getStreet());
        dto.setArea(address.getArea());
        dto.setCity(address.getCity());
        dto.setDistrict(address.getDistrict());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        dto.setPrimary(address.getIsPrimary());
        return dto;
    }
}