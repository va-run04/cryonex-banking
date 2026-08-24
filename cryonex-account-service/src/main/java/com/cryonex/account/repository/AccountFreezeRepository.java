package com.cryonex.account.repository;

import com.cryonex.account.entity.AccountFreeze;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountFreezeRepository extends JpaRepository<AccountFreeze, String> {

    List<AccountFreeze> findByAccountId(String accountId);

}