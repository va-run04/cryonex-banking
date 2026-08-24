package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccountAuditRepository extends JpaRepository<AccountAudit, String>, JpaSpecificationExecutor<AccountAudit> {

    List<AccountAudit> findByAccountIdOrderByCreatedDateDesc(String accountId);

}