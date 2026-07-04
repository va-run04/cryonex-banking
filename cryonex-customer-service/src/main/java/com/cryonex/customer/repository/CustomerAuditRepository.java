package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerAuditRepository extends JpaRepository<CustomerAudit, String>, JpaSpecificationExecutor<CustomerAudit> {

    List<CustomerAudit> findByCustomerOrderByCreatedDateDesc(Customer customer);

}