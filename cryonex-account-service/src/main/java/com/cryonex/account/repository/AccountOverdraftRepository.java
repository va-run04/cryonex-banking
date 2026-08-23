package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountOverdraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountOverdraftRepository extends JpaRepository<AccountOverdraft, String> {

    Optional<AccountOverdraft> findByAccountId(String accountId);

}