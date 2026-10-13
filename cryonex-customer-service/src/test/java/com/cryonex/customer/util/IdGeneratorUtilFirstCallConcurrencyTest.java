package com.cryonex.customer.util;

import com.cryonex.customer.repository.IdSequenceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
public class IdGeneratorUtilFirstCallConcurrencyTest {


    @Autowired
    private IdGeneratorUtil idGeneratorUtil;

    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;


    @BeforeEach
    public void emptyCounterTable(){
        assertTrue(dataSourceUrl.contains("cryonex_test"), "Refusing to run: not connected to the test database  -> "+dataSourceUrl);
        idSequenceRepository.deleteAll();
    }

    @Test
    public void generateId_shouldNeverFailOrDuplicate_whenFirstCallsAreConcurrent() throws Exception{
        int threadCount = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch allReady = new CountDownLatch(threadCount);
        CountDownLatch startGun = new CountDownLatch(1);


        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            results.add(pool.submit(() -> {
                allReady.countDown();
                startGun.await();
                return idGeneratorUtil.generateId("CUS", "CUSTOMER");
            }));
        }

        allReady.await();
        startGun.countDown();

        Set<String> uniqueIds = new HashSet<>();
        int failures = 0;
        for(Future<String> result : results){
            try{
                uniqueIds.add(result.get());
            }catch (ExecutionException e){
                failures++;
                System.out.println("CALL FAILED: "+ e.getCause());
            }
        }
        pool.shutdown();

        System.out.println("RESULT -> failures: " + failures + ", unique IDs: " + uniqueIds.size());
        assertEquals(0, failures, "Some calls threw exceptions");
        assertEquals(threadCount, uniqueIds.size(), "Duplicate IDs were generated");
    }


}
