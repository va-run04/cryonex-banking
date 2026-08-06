package com.cryonex.account.util;

import com.cryonex.account.entity.IdSequence;
import com.cryonex.account.repository.IdSequenceRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorUtil {

    private final IdSequenceRepository idSequenceRepository;

    public IdGeneratorUtil(IdSequenceRepository idSequenceRepository){
        this.idSequenceRepository = idSequenceRepository;
    }

    @Transactional
    public String generateId(String prefix, String entityName){

        IdSequence sequence = idSequenceRepository.findByEntityNameToUpdate(entityName).orElse(null);

        if(sequence == null){
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
