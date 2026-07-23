package com.cryonex.customer.service;

import com.cryonex.customer.dto.request.MarketingUpdateRequestDto;
import com.cryonex.customer.dto.request.NotificationUpdateRequestDto;
import com.cryonex.customer.dto.request.PreferenceRequestDto;
import com.cryonex.customer.dto.request.PreferenceUpdateRequestDto;
import com.cryonex.customer.dto.response.PreferenceResponseDto;
import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import com.cryonex.customer.entity.CustomerPreference;
import com.cryonex.customer.exception.BusinessValidationException;
import com.cryonex.customer.exception.ResourceNotFoundException;
import com.cryonex.customer.repository.CustomerAuditRepository;
import com.cryonex.customer.repository.CustomerPreferenceRepository;
import com.cryonex.customer.repository.CustomerRepository;
import com.cryonex.customer.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreferenceService {

    private final CustomerPreferenceRepository preferenceRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAuditRepository customerAuditRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    public PreferenceService(CustomerPreferenceRepository preferenceRepository,
                             CustomerRepository customerRepository,
                             CustomerAuditRepository customerAuditRepository,
                             IdGeneratorUtil idGeneratorUtil) {
        this.preferenceRepository = preferenceRepository;
        this.customerRepository = customerRepository;
        this.customerAuditRepository = customerAuditRepository;
        this.idGeneratorUtil = idGeneratorUtil;
    }


    //1) CREATE PREFERENCE
    @Transactional
    public PreferenceResponseDto createPreference(String customerId, PreferenceRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_001", "Customer not found."));

        if (preferenceRepository.findByCustomer(customer).isPresent()) {
            throw new BusinessValidationException("PREF_002", "Preferences already exist for this customer.");
        }

        String preferenceId = idGeneratorUtil.generateId("PREF", "PREFERENCE");

        CustomerPreference preference = new CustomerPreference();
        preference.setPreferenceId(preferenceId);
        preference.setCustomer(customer);
        preference.setLanguage(request.getLanguage());
        preference.setCommunicationMode(request.getCommunicationMode());
        preference.setEmailEnabled(request.getEmailEnabled() != null ? request.getEmailEnabled() : true);
        preference.setSmsEnabled(request.getSmsEnabled() != null ? request.getSmsEnabled() : true);
        preference.setMarketingEnabled(request.getMarketingEnabled() != null ? request.getMarketingEnabled() : false);

        CustomerPreference savedPreference = preferenceRepository.save(preference);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("PREFERENCE_CREATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Preferences created");
        customerAuditRepository.save(audit);

        PreferenceResponseDto response = new PreferenceResponseDto();
        response.setPreferenceId(savedPreference.getPreferenceId());
        response.setLanguage(savedPreference.getLanguage());
        response.setCommunicationMode(savedPreference.getCommunicationMode());
        response.setEmailEnabled(savedPreference.getEmailEnabled());
        response.setSmsEnabled(savedPreference.getSmsEnabled());
        response.setMarketingEnabled(savedPreference.getMarketingEnabled());

        return response;
    }

    // 2) GET PREFERENCES
    public PreferenceResponseDto getPreferences(String customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_001", "Customer not found."));

        CustomerPreference preference = preferenceRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_003", "Preferences not found for this customer."));

        PreferenceResponseDto response = new PreferenceResponseDto();
        response.setPreferenceId(preference.getPreferenceId());
        response.setLanguage(preference.getLanguage());
        response.setCommunicationMode(preference.getCommunicationMode());
        response.setEmailEnabled(preference.getEmailEnabled());
        response.setSmsEnabled(preference.getSmsEnabled());
        response.setMarketingEnabled(preference.getMarketingEnabled());

        return response;
    }

    // 3) UPDATE PREFERENCES
    @Transactional
    public PreferenceResponseDto updatePreferences(String customerId, PreferenceUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_001", "Customer not found."));

        CustomerPreference preference = preferenceRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_003", "Preferences not found for this customer."));

        if (request.getLanguage() != null) {
            preference.setLanguage(request.getLanguage());
        }
        if (request.getCommunicationMode() != null) {
            preference.setCommunicationMode(request.getCommunicationMode());
        }
        if (request.getEmailEnabled() != null) {
            preference.setEmailEnabled(request.getEmailEnabled());
        }
        if (request.getSmsEnabled() != null) {
            preference.setSmsEnabled(request.getSmsEnabled());
        }
        if (request.getMarketingEnabled() != null) {
            preference.setMarketingEnabled(request.getMarketingEnabled());
        }

        CustomerPreference updatedPreference = preferenceRepository.save(preference);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("PREFERENCE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Preferences updated");
        customerAuditRepository.save(audit);

        PreferenceResponseDto response = new PreferenceResponseDto();
        response.setPreferenceId(updatedPreference.getPreferenceId());
        response.setLanguage(updatedPreference.getLanguage());
        response.setCommunicationMode(updatedPreference.getCommunicationMode());
        response.setEmailEnabled(updatedPreference.getEmailEnabled());
        response.setSmsEnabled(updatedPreference.getSmsEnabled());
        response.setMarketingEnabled(updatedPreference.getMarketingEnabled());

        return response;
    }

    // 4) UPDATE NOTIFICATION PREFERENCES
    @Transactional
    public PreferenceResponseDto updateNotificationPreferences(String customerId, NotificationUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_001", "Customer not found."));

        CustomerPreference preference = preferenceRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_003", "Preferences not found for this customer."));

        if (request.getEmailEnabled() != null) {
            preference.setEmailEnabled(request.getEmailEnabled());
        }
        if (request.getSmsEnabled() != null) {
            preference.setSmsEnabled(request.getSmsEnabled());
        }

        CustomerPreference updatedPreference = preferenceRepository.save(preference);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("NOTIFICATION_PREFERENCE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Email: " + updatedPreference.getEmailEnabled() + ", SMS: " + updatedPreference.getSmsEnabled());
        customerAuditRepository.save(audit);

        PreferenceResponseDto response = new PreferenceResponseDto();
        response.setPreferenceId(updatedPreference.getPreferenceId());
        response.setLanguage(updatedPreference.getLanguage());
        response.setCommunicationMode(updatedPreference.getCommunicationMode());
        response.setEmailEnabled(updatedPreference.getEmailEnabled());
        response.setSmsEnabled(updatedPreference.getSmsEnabled());
        response.setMarketingEnabled(updatedPreference.getMarketingEnabled());

        return response;
    }


    // 5) UPDATE MARKETING PREFERENCES
    @Transactional
    public PreferenceResponseDto updateMarketingPreference(String customerId, MarketingUpdateRequestDto request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_001", "Customer not found."));

        CustomerPreference preference = preferenceRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("PREF_003", "Preferences not found for this customer."));

        preference.setMarketingEnabled(request.getMarketingEnabled());

        CustomerPreference updatedPreference = preferenceRepository.save(preference);

        CustomerAudit audit = new CustomerAudit();
        audit.setAuditId(idGeneratorUtil.generateId("AUD", "AUDIT"));
        audit.setCustomer(customer);
        audit.setAction("MARKETING_PREFERENCE_UPDATED");
        audit.setPerformedBy("SYSTEM");
        audit.setNewValue("Marketing enabled: " + updatedPreference.getMarketingEnabled());
        customerAuditRepository.save(audit);

        PreferenceResponseDto response = new PreferenceResponseDto();
        response.setPreferenceId(updatedPreference.getPreferenceId());
        response.setLanguage(updatedPreference.getLanguage());
        response.setCommunicationMode(updatedPreference.getCommunicationMode());
        response.setEmailEnabled(updatedPreference.getEmailEnabled());
        response.setSmsEnabled(updatedPreference.getSmsEnabled());
        response.setMarketingEnabled(updatedPreference.getMarketingEnabled());

        return response;
    }
}