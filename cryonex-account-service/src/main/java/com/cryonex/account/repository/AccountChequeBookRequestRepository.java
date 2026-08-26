package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountChequeBookRequest;
import com.cryonex.account.enums.ChequeBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AccountChequeBookRequestRepository extends JpaRepository<AccountChequeBookRequest, String>, JpaSpecificationExecutor<AccountChequeBookRequest> {

    List<AccountChequeBookRequest> findByAccountId(String accountId);

    Optional<AccountChequeBookRequest> findByAccountIdAndRequestStatusIn(String accountId, List<ChequeBookStatus> statuses);

}