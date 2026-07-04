package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerPreferenceRepository extends JpaRepository<CustomerPreference, String> {

    Optional<CustomerPreference> findByCustomer(Customer customer);

}