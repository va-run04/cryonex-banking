package com.cryonex.customer.util;

import org.springframework.stereotype.Component;

@Component
public class IdGeneratorUtil {

    public String generateId(String prefix, Long sequenceNumber){

        return prefix + sequenceNumber;

    }
}
