package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountPassbookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccountPassbookRequestRepository extends JpaRepository<AccountPassbookRequest, String>, JpaSpecificationExecutor<AccountPassbookRequest> {

    List<AccountPassbookRequest> findByAccountId(String accountId);

}