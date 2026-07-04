package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, String> {

    List<CustomerAddress> findByCustomer(Customer customer);

}
