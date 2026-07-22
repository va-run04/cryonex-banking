package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.ContactRequestDto;
import com.cryonex.customer.dto.request.EmailUpdateRequestDto;
import com.cryonex.customer.dto.request.MobileUpdateRequestDto;
import com.cryonex.customer.dto.request.PreferredContactModeUpdateRequestDto;
import com.cryonex.customer.dto.response.ContactResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.entity.CustomerContact;
import com.cryonex.customer.enums.ContactMode;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.DuplicateResourceException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerContactRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final CustomerContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public ContactService(CustomerContactRepository contactRepository,
                          CustomerRepository customerRepository,
                          CustomerAuditRepository customerAuditRepository,
                          IdGeneratorUtil idGeneratorUtil) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }


    // 1) ADD CONTACT
    @Transactional
    public ContactResponseDto addContact(String customerId, ContactRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_001", "Customer does not exist."));

        if (contactRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateResourceException("CNT_002", "Mobile number already exists.");
        }

        if (request.getAlternateMobile() != null &&
                request.getAlternateMobile().equals(request.getMobileNumber())) {
            throw new BusinessValidationException("CNT_003", "Alternate mobile cannot be the same as primary mobile.");
        }

        if (request.getEmail() != null && contactRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("CNT_004", "Email already exists.");
        }

        String contactId = idGeneratorUtil.generateId("CNT", "CONTACT");

        CustomerContact contact = new CustomerContact();
        contact.setContactId(contactId);
        contact.setCustomer(customer);
        contact.setMobileNumber(request.getMobileNumber());
        contact.setAlternateMobile(request.getAlternateMobile());
        contact.setEmail(request.getEmail());
        contact.setLandline(request.getLandline());
        contact.setPreferredContactMode(request.getPreferredContactMode());

        CustomerContact savedContact = contactRepository.save(contact);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("CONTACT_ADDED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Contact details added");
        customerAuditRepository.save(audit);

        ContactResponseDto response = new ContactResponseDto();
        response.setContactId(savedContact.getContactId());
        response.setMobileNumber(savedContact.getMobileNumber());
        response.setAlternateMobile(savedContact.getAlternateMobile());
        response.setEmail(savedContact.getEmail());
        response.setLandline(savedContact.getLandline());
        response.setPreferredContactMode(savedContact.getPreferredContactMode());
        return response;
    }


    // 2) GET CONTACT DETAILS
    public ContactResponseDto getContactDetails(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_001", "Customer does not exist."));

        CustomerContact contact = contactRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_004", "Customer contact details not found."));

        ContactResponseDto response = new ContactResponseDto();
        response.setContactId(contact.getContactId());
        response.setMobileNumber(contact.getMobileNumber());
        response.setAlternateMobile(contact.getAlternateMobile());
        response.setEmail(contact.getEmail());
        response.setLandline(contact.getLandline());
        response.setPreferredContactMode(contact.getPreferredContactMode());

        return response;
    }

    // 3) UPDATE MOBILE
    @Transactional
    public void updateMobile(String customerId, MobileUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_001", "Customer does not exist."));

        CustomerContact contact = contactRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_004", "Customer contact details not found."));

        if (contactRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateResourceException("CNT_002", "Mobile number already registered.");
        }

        String oldMobile = contact.getMobileNumber();
        contact.setMobileNumber(request.getMobileNumber());

        contactRepository.save(contact);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("MOBILE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue(oldMobile);
        audit.setNewValue(request.getMobileNumber());
        customerAuditRepository.save(audit);

    }

    // 4) UPDATE EMAIL
    @Transactional
    public void updateEmail(String customerId, EmailUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_001", "Customer does not exist."));

        CustomerContact contact = contactRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_004", "Customer contact details not found."));

        if (contactRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("CNT_003", "Email address already exists.");
        }

        String oldEmail = contact.getEmail();
        contact.setEmail(request.getEmail());

        contactRepository.save(contact);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("EMAIL_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue(oldEmail);
        audit.setNewValue(request.getEmail());
        customerAuditRepository.save(audit);

    }
    // 5) UPDATE PREFERRED CONTACT MODE
    @Transactional
    public void updatePreferredContactMode(String customerId, PreferredContactModeUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_001", "Customer does not exist."));

        CustomerContact contact = contactRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("CNT_004", "Customer contact details not found."));

        ContactMode oldMode = contact.getPreferredContactMode();
        contact.setPreferredContactMode(request.getPreferredContactMode());

        contactRepository.save(contact);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));        audit.setCustomer(customer);
        audit.setAction("PREFERRED_CONTACT_MODE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setOldValue(oldMode != null ? oldMode.toString() : null);
        audit.setNewValue(request.getPreferredContactMode().toString());
        customerAuditRepository.save(audit);

    }
}