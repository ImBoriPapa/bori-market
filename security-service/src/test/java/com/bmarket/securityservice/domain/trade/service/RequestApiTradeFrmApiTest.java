package com.bmarket.securityservice.domain.trade.service;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.internal_api.trade.RequestTradeApi;
import com.bmarket.securityservice.internal_api.trade.form.RequestTradeForm;

import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;

import com.bmarket.securityservice.exception.custom_exception.internal_api_ex.InternalRequestFailException;
import com.bmarket.securityservice.internal_api.trade.RequestTradeApiImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;

import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class RequestApiTradeFrmApiTest {

    @Autowired
    RequestTradeApi requestTradeApi;

    @Autowired
    ObjectMapper objectMapper;

    public MockWebServer mockWebServer;
    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer.start(8081);
    }
    @Disabled
    @Test
    @DisplayName("거래 생성 요청 테스트")
    void RequestCreateTradeTest() throws Exception {
        //given

        MockMultipartFile file1 = new MockMultipartFile("images", "test1.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("images", "test2.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("images", "test3.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file4 = new MockMultipartFile("images", "test4.jpg", "jpg", "test1".getBytes());
        MockMultipartFile file5 = new MockMultipartFile("images", "test5.jpg", "jpg", "test1".getBytes());
        List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
        Long accountId = 1L;
        RequestTradeForm form = RequestTradeForm.builder()
                .nickname("trader")
                .title("맥북프로")
                .context("사용한지 1년 안된 맥북프로 네고 사절")
                .category(Category.DIGITAL)
                .price(1000000)
                .isShare(false)
                .isOffer(false).build();
        //when
        ResponseCreateTradeResult createTradeResult = requestTradeApi.requestCreateTrade(accountId, form, files);

        //then
        log.info("result id ={}", createTradeResult.getTradeId());
        log.info("result at={}", createTradeResult.getCreatedAt());
    }

    // TODO: 2022/11/16 테스트 보강
    @Disabled
    @Test
    @DisplayName("거래내역 조회 요청 테스트")
    void requestGetSaleHistoryTest() throws Exception {
        //given
        for (int i = 0; i < 20; i++) {

            MockMultipartFile file1 = new MockMultipartFile("images", "test1.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("images", "test2.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file3 = new MockMultipartFile("images", "test3.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file4 = new MockMultipartFile("images", "test4.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file5 = new MockMultipartFile("images", "test5.jpg", "jpg", "test1".getBytes());
            List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
            Long accountId = Long.valueOf(i);
            RequestTradeForm form = RequestTradeForm.builder()
                    .nickname("trader" + i)
                    .title("맥북프로" + i)
                    .context("사용한지 1년 안된 맥북프로 네고 사절")
                    .category(Category.DIGITAL)
                    .price(1000000)
                    .isShare(false)
                    .isOffer(false).build();

            requestTradeApi.requestCreateTrade(accountId, form, files);
        }
        //when

        //when
        List<RequestGetTradeListResult> results = requestTradeApi.requestGetSaleHistory(1L);
        //then
        for (RequestGetTradeListResult listResult : results) {
            log.info("result title={}", listResult.getTitle());
            log.info("result tradeId={}", listResult.getTradeId());
        }
    }
    @Disabled
    @Test
    @DisplayName("거래 목록 조회 테스트")
    void requestGetTradeTest() throws Exception {
        for (int i = 0; i < 20; i++) {

            MockMultipartFile file1 = new MockMultipartFile("images", "test1.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("images", "test2.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file3 = new MockMultipartFile("images", "test3.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file4 = new MockMultipartFile("images", "test4.jpg", "jpg", "test1".getBytes());
            MockMultipartFile file5 = new MockMultipartFile("images", "test5.jpg", "jpg", "test1".getBytes());
            List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4, file5);
            Long accountId = Long.valueOf(i);
            RequestTradeForm form = RequestTradeForm.builder()
                    .nickname("trader" + i)
                    .title("맥북프로" + i)
                    .context("사용한지 1년 안된 맥북프로 네고 사절")
                    .category(Category.DIGITAL)
                    .price(1000000)
                    .isShare(false)
                    .isOffer(false).build();

            requestTradeApi.requestCreateTrade(accountId, form, files);
        }

        TradeDetailResult getTrade = requestTradeApi.requestGetTrade(1L);
        log.info("getTrade={}",getTrade);
    }
}