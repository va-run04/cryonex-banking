package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerContactRepository extends JpaRepository<CustomerContact, String> {

    Optional<CustomerContact> findByCustomer(Customer customer);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);
}
