package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountClosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountClosureRepository extends JpaRepository<AccountClosure, String> {

    Optional<AccountClosure> findByAccountIdAndClosureStatus(String accountId, String closureStatus);

}