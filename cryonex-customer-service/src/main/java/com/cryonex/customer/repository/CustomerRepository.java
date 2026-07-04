package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer, String> , JpaSpecificationExecutor<Customer> {

    boolean existsByPanNumber(String panNumber);

    boolean existsByAadhaarNumber(String aadhaarNumber);

    boolean existsByCifNumber(String cifNumber);

}
