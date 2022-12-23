package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.TestData;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
@Transactional
class TradeQueryRepositoryImplTest {

    @Autowired
    TradeQueryRepository tradeQueryRepository;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository imageRepository;
    @Autowired
    TestData testData;

    @BeforeEach
    void before() {
        log.info("[TEST DATA INIT]");
        testData.initSample1();
        testData.initSample2();
        testData.initSample3();
        testData.initSample4();
        log.info("[TEST DATA INIT FINISH]");
    }



    @AfterEach
    void after() {
        log.info("[TEST DATA DELETE]");
        tradeRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    @DisplayName("리스트 조회")
    void findTradeListWithConditionTest1() throws Exception {
        //given
        log.info("[test]");
        //when

        //then

    }

}