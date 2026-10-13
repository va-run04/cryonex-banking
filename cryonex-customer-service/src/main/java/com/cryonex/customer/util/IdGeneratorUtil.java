package com.cryonex.customer.util;

import com.cryonex.customer.entity.IdSequence;
import com.cryonex.customer.repository.IdSequenceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdGeneratorUtil {

    private final IdSequenceRepository idSequenceRepository;

    public IdGeneratorUtil(IdSequenceRepository idSequenceRepository) {
        this.idSequenceRepository = idSequenceRepository;
    }

    @Transactional
    public String generateId(String prefix, String entityName) {

        idSequenceRepository.insertIfAbsent(entityName, 100000L);

        IdSequence sequence = idSequenceRepository.findByEntityNameToUpdate(entityName)
                .orElseThrow(() -> new IllegalStateException(
                        "ID counter row missing for entity: " + entityName));

        long nextValue = sequence.getCurrentValue() + 1;
        sequence.setCurrentValue(nextValue);
        idSequenceRepository.save(sequence);

        return prefix + nextValue;
    }

}