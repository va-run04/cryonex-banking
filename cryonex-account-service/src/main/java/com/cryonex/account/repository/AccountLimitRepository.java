package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLimitRepository extends JpaRepository<AccountLimit, String> {

    Optional<AccountLimit> findByAccountId(String accountId);

}