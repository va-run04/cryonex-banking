package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountStatementPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountStatementPreferenceRepository extends JpaRepository<AccountStatementPreference, String> {

    Optional<AccountStatementPreference> findByAccountId(String accountId);

}