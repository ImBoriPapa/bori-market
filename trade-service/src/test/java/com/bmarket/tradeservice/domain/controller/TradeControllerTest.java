package com.bmarket.tradeservice.domain.controller;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.dto.RequestUpdateForm;
import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
class TradeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TradeCommandService tradeCommandService;
    private MockWebServer mockWebServer;
    private Dispatcher dispatcher;

    @BeforeEach
    void beforeEach() throws IOException {

        String imagePath1 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi1.png";
        String imagePath2 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi2.png";
        String imagePath3 = "http://localhost:8095/frm/trade/dasfs432mkfdsfi3.png";
        String tradeImageId = UUID.randomUUID().toString();

        ResponseImageDto imageDto = new ResponseImageDto(true, tradeImageId, List.of(imagePath1, imagePath2, imagePath3));

        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/trade-image");

        MockResponse postResponse = new MockResponse();
        postResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        postResponse.setBody(objectMapper.writeValueAsString(imageDto));

        MockResponse deleteResponse = new MockResponse();
        deleteResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        deleteResponse.setBody(objectMapper.writeValueAsString(new ResponseImageDto(true, tradeImageId, new ArrayList<>())));

        MockResponse putResponse = new MockResponse();
        putResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        putResponse.setBody(objectMapper.writeValueAsString(new ResponseImageDto(true, tradeImageId, List.of(imagePath1, imagePath2, imagePath3))));

        dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
                if (request.getMethod().equals("POST")) {
                    return postResponse;
                }

                if (request.getMethod().equals("DELETE")) {
                    return deleteResponse;
                }
                if (request.getMethod().equals("PUT")) {
                    return putResponse;
                }

                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
        dispatcher.shutdown();
    }

    @Test
    @DisplayName("판매글 생성 테스트")
    void createTradeTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("동네").build();

        RequestForm requestForm = RequestForm.builder()
                .accountId(1L)
                .title("판매글 테스트 제목")
                .context("판매글 테스트 내용")
                .price(10000)
                .category(Category.BOOK)
                .address(address)
                .isShare(false)
                .isOffer(false)
                .build();

        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());
        MockMultipartFile form = new MockMultipartFile("form", "form", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestForm));
        //when
        //then
        mockMvc.perform(multipart("/trade")
                        .file(images1)
                        .file(images2)
                        .file(images3)
                        .file(form)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("판매글 삭제 테스트")
    void deleteTradeTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("동네").build();

        RequestForm requestForm = RequestForm.builder()
                .accountId(1L)
                .title("판매글 테스트 제목")
                .context("판매글 테스트 내용")
                .price(10000)
                .category(Category.BOOK)
                .address(address)
                .isShare(false)
                .isOffer(false)
                .build();

        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());

        //when
        Trade trade = tradeCommandService.createTrade(requestForm, List.of(images1, images2, images3));

        //then
        mockMvc.perform(delete("/trade/{tradeId}", trade.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").value(trade.getId()))
                .andExpect(jsonPath("createdAt").isEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("판매글 수정 테스트")
    void putTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("동네").build();

        RequestForm requestForm = RequestForm.builder()
                .accountId(1L)
                .title("판매글 테스트 제목")
                .context("판매글 테스트 내용")
                .price(10000)
                .category(Category.BOOK)
                .address(address)
                .isShare(false)
                .isOffer(false)
                .build();

        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());
        MockMultipartFile images4 = new MockMultipartFile("images", "image4.png", "image/png", "image".getBytes());
        MockMultipartFile images5 = new MockMultipartFile("images", "image5.png", "image/png", "image".getBytes());
        MockMultipartFile images6 = new MockMultipartFile("images", "image6.png", "image/png", "image".getBytes());

        Address newAddress = Address.builder()
                .addressCode(2001)
                .city("인천")
                .district("인천구")
                .town("인천동").build();

        RequestUpdateForm requestUpdateForm = RequestUpdateForm.builder()
                .title("판매글 테스트 제목 변경")
                .context("판매글 테스트 내용 변경")
                .price(12000)
                .category(Category.ETC)
                .address(newAddress)
                .isShare(true)
                .isOffer(true)
                .build();

        //when
        Trade savedTrade = tradeCommandService.createTrade(requestForm, List.of(images1, images2, images3));

        //then
        MockMultipartHttpServletRequestBuilder builder = multipart("/trade/{tradeId}", savedTrade.getId());
        builder.with(new RequestPostProcessor() {

            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(images4)
                        .file(images5)
                        .file(images6)
                        .file(new MockMultipartFile("form", "form", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestUpdateForm)))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").value(savedTrade.getId()))
                .andDo(print());
    }

}