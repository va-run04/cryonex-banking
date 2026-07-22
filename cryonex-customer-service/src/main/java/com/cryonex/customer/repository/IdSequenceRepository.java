package com.cryonex.customer.repository;

import com.cryonex.customer.entity.IdSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdSequenceRepository extends JpaRepository<IdSequence, String> {

}