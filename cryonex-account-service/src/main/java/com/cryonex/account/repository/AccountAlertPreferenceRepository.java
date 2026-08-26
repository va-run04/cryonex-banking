package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountAlertPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountAlertPreferenceRepository extends JpaRepository<AccountAlertPreference, String> {

    Optional<AccountAlertPreference> findByAccountId(String accountId);

}