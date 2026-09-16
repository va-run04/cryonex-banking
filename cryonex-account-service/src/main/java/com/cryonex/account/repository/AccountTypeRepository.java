package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByTypeCode(String typeCode);
}