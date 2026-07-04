package com.cryonex.customer.repository;

import com.cryonex.customer.entity.Customer;
import com.cryonex.customer.entity.CustomerDocument;
import com.cryonex.customer.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, String> {

    List<CustomerDocument> findByCustomer(Customer customer);

    Optional<CustomerDocument> findByCustomerAndDocumentType(Customer customer, DocumentType documentType);

}