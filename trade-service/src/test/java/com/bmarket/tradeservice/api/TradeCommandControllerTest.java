package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.api.requestForm.RequestForm;
import com.bmarket.tradeservice.api.requestForm.RequestUpdateForm;
import com.bmarket.tradeservice.domain.entity.*;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import com.bmarket.tradeservice.status.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@Slf4j
class TradeCommandControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeCommandService tradeCommandService;

    @BeforeEach
    void beforeEach() {
        log.info("[TEST DATA INIT]");
        Address address = Address.builder()
                .addressCode(1001)
                .city("??????")
                .district("?????????")
                .town("?????????")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId("testMember")
                .title("??????1")
                .context("??????1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS)
                .build();
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "image".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "image".getBytes());
        tradeCommandService.createTrade(form, List.of(image1, image2, image3));
        log.info("[TEST DATA INIT FINISH]");
    }

    @AfterEach
    void after() {
        log.info("[TEST DATA DELETE]");
        Trade testMember = tradeRepository.findByMemberId("testMember").get();
        tradeCommandService.deleteTrade(testMember.getId());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void postTradeTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("??????")
                .district("?????????")
                .town("?????????")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId("member")
                .title("??????1")
                .context("??????1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS)
                .build();

        MockMultipartFile requestForm = new MockMultipartFile("form", "RequestForm", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(form));
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        //when
        mockMvc.perform(multipart("/trade")
                        .file(image1)
                        .file(requestForm)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("status").value(ResponseStatus.SUCCESS.name()))
                .andExpect(jsonPath("code").value(ResponseStatus.SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.SUCCESS.getMessage()))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.tradeId").exists())
                .andExpect(jsonPath("result.memberId").value("member"))
                .andExpect(jsonPath("result.createdAt").exists())
                .andExpect(jsonPath("result.links").isArray())
                .andDo(print())
                .andDo(document("createTrade",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestPartFields("form",
                                fieldWithPath("memberId").description("?????? ?????????"),
                                fieldWithPath("title").description("??????"),
                                fieldWithPath("context").description("??????"),
                                fieldWithPath("price").description("??????"),
                                fieldWithPath("address.addressCode").description("?????? ??????"),
                                fieldWithPath("address.city").description("??????"),
                                fieldWithPath("address.district").description("???"),
                                fieldWithPath("address.town").description("??????"),
                                fieldWithPath("category").description("????????????"),
                                fieldWithPath("isOffer").description("????????????"),
                                fieldWithPath("tradeType").description("?????? ??????")
                        ),
                        requestPartBody("images"),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("code").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("result").description("?????? ??????"),
                                fieldWithPath("result.tradeId").description(""),
                                fieldWithPath("result.memberId").description(""),
                                fieldWithPath("result.createdAt").description(""),
                                fieldWithPath("result.links").description(""),
                                fieldWithPath("result.links.[].rel").description(""),
                                fieldWithPath("result.links.[].href").description("")
                        )
                ));
        //then

    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void patchTradeTest() throws Exception {
        //given
        RequestUpdateForm updateForm = RequestUpdateForm.builder()
                .title("???????????????")
                .context("???????????????")
                .price(30000)
                .build();
        //when
        Trade trade = tradeRepository.findByMemberId("testMember").get();

        MockMultipartHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/trade/{tradeId}", trade.getId());

        request.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });
        //then
        mockMvc.perform(request
                        .file(new MockMultipartFile("form", "RequestForm", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(updateForm)))
                        .file(new MockMultipartFile("images", "image.png", MediaType.IMAGE_PNG.getType(), "image".getBytes()))
                )
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(ResponseStatus.SUCCESS.name()))
                .andExpect(jsonPath("code").value(ResponseStatus.SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.SUCCESS.getMessage()))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.tradeId").exists())
                .andExpect(jsonPath("result.memberId").value("testMember"))
                .andExpect(jsonPath("result.createdAt").exists())
                .andExpect(jsonPath("result.links").isArray())
                .andDo(print());


    }

    @Test
    @DisplayName("?????? ??? ???????????? ?????????")
    void patchStatusTest() throws Exception {
        //given
        Trade trade = tradeRepository.findByMemberId("testMember").get();
        //when
        mockMvc.perform(patch("/trade/{memberId}/status", trade.getId())
                        .param("set", TradeStatus.SOLD_OUT.name()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("status").value(ResponseStatus.SUCCESS.name()))
                .andExpect(jsonPath("code").value(ResponseStatus.SUCCESS.getCode()))
                .andExpect(jsonPath("message").value(ResponseStatus.SUCCESS.getMessage()))
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.tradeId").exists())
                .andExpect(jsonPath("result.memberId").value("testMember"))
                .andExpect(jsonPath("result.createdAt").exists())
                .andExpect(jsonPath("result.links").isArray())
                .andDo(print());
        //then

    }
}