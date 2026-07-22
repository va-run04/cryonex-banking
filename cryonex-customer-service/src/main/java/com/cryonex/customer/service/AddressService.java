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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Transactional
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

        String addressId = idGeneratorUtil.generateId("ADDR", "ADDRESS");

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
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("ADDRESS_ADDED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Address added: " + request.getAddressType());
        customerAuditRepository.save(audit);

        AddressResponseDto response = new AddressResponseDto();
        response.setAddressId(savedAddress.getAddressId());
        response.setAddressType(savedAddress.getAddressType());
        response.setDoorNumber(savedAddress.getDoorNumber());
        response.setStreet(savedAddress.getStreet());
        response.setArea(savedAddress.getArea());
        response.setCity(savedAddress.getCity());
        response.setDistrict(savedAddress.getDistrict());
        response.setState(savedAddress.getState());
        response.setCountry(savedAddress.getCountry());
        response.setPostalCode(savedAddress.getPostalCode());
        response.setPrimary(savedAddress.getIsPrimary());

        return response;
    }

    // 2) GET CUSTOMER ADDRESSES
    public List<AddressResponseDto> getAddresses(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_001", "Customer not found."));

        List<CustomerAddress> addresses = addressRepository.findByCustomer(customer);

        List<AddressResponseDto> responseList = new ArrayList<>();

        for (CustomerAddress address : addresses) {
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
            responseList.add(dto);
        }

        return responseList;
    }

    // 3) UPDATE ADDRESS
    @Transactional
    public AddressResponseDto updateAddress(String customerId, String addressId, AddressUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_001", "Customer not found."));

        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_002", "Address not found."));

        if (request.isPrimary()) {
            List<CustomerAddress> existingAddresses = addressRepository.findByCustomer(customer);
            for (CustomerAddress existing : existingAddresses) {
                if (existing.getIsPrimary() && !existing.getAddressId().equals(addressId)) {
                    existing.setIsPrimary(false);
                    addressRepository.save(existing);
                }
            }
        }

        if (request.getDoorNumber() != null) {
            address.setDoorNumber(request.getDoorNumber());
        }
        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getArea() != null) {
            address.setArea(request.getArea());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getDistrict() != null) {
            address.setDistrict(request.getDistrict());
        }
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }
        address.setIsPrimary(request.isPrimary());

        CustomerAddress updatedAddress = addressRepository.save(address);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("ADDRESS_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Address updated: " + addressId);
        customerAuditRepository.save(audit);

        AddressResponseDto response = new AddressResponseDto();
        response.setAddressId(updatedAddress.getAddressId());
        response.setAddressType(updatedAddress.getAddressType());
        response.setDoorNumber(updatedAddress.getDoorNumber());
        response.setStreet(updatedAddress.getStreet());
        response.setArea(updatedAddress.getArea());
        response.setCity(updatedAddress.getCity());
        response.setDistrict(updatedAddress.getDistrict());
        response.setState(updatedAddress.getState());
        response.setCountry(updatedAddress.getCountry());
        response.setPostalCode(updatedAddress.getPostalCode());
        response.setPrimary(updatedAddress.getIsPrimary());

        return response;
    }


    // 4) DELETE ADDRESS
    @Transactional
    public void deleteAddress(String customerId, String addressId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_001", "Customer not found."));

        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ADDR_002", "Address not found."));

        if (address.getIsPrimary()) {
            throw new BusinessValidationException("ADDR_003", "Primary address cannot be deleted.");
        }

        addressRepository.delete(address);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("ADDRESS_DELETED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue("Address deleted: " + addressId);
        customerAuditRepository.save(audit);

    }


}