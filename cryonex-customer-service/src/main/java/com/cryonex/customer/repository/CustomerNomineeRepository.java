package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerNominee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerNomineeRepository extends JpaRepository<CustomerNominee, String> {

    List<CustomerNominee> findByCustomer(Customer customer);

}