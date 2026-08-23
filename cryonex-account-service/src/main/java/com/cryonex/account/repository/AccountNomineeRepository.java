package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountNominee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountNomineeRepository extends JpaRepository<AccountNominee, String> {

    List<AccountNominee> findByAccountId(String accountId);

}