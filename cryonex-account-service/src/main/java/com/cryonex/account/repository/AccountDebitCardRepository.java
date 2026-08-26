package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountDebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccountDebitCardRepository extends JpaRepository<AccountDebitCard, String>, JpaSpecificationExecutor<AccountDebitCard> {

    List<AccountDebitCard> findByAccountId(String accountId);

}