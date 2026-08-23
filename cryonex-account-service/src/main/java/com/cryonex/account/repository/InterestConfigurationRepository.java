package com.cryonex.account.repository;

import com.cryonex.account.entity.InterestConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestConfigurationRepository extends JpaRepository<InterestConfiguration, String> {

    List<InterestConfiguration> findByAccountType(String accountType);

}