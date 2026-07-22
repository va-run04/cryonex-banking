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

        IdSequence sequence = idSequenceRepository.findById(entityName).orElse(null);

        if (sequence == null) {
            sequence = new IdSequence();
            sequence.setEntityName(entityName);
            sequence.setCurrentValue(100000L);
        }

        long nextValue = sequence.getCurrentValue() + 1;
        sequence.setCurrentValue(nextValue);
        idSequenceRepository.save(sequence);

        return prefix + nextValue;
    }

}