package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerKyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerKycRepository extends JpaRepository<CustomerKyc, String> {

    Optional<CustomerKyc> findByCustomer(Customer customer);

}