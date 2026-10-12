package com.cryonex.customer.repository;

import com.cryonex.customer.entity.IdSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface IdSequenceRepository extends JpaRepository<IdSequence, String> {

    @Query("SELECT s FROM IdSequence s WHERE s.entityName = :entityName")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IdSequence> findByEntityNameToUpdate(@Param("entityName") String entityName);

}