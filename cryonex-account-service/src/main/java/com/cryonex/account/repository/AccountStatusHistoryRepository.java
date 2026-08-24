package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountStatusHistoryRepository extends JpaRepository<AccountStatusHistory, String> {

    List<AccountStatusHistory> findByAccountIdOrderByCreatedDateDesc(String accountId);

}