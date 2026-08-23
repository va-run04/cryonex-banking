package com.cryonex.account.repository;

import com.cryonex.account.entity.JointAccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JointAccountHolderRepository  extends JpaRepository<JointAccountHolder, String> {

    List<JointAccountHolder> findByAccountId(String accountId);

}
