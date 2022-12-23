package com.bmarket.tradeservice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class TradeInformationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("카테고리 목록")
    void getCategory() throws Exception {
        //given
        mockMvc.perform(get("/trade/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("names").isNotEmpty())
                .andDo(print())
                .andDo(document(
                        "category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("names").description("카테고리 명칭")
                        )
                ));
        //when

        //then

    }

    @Test
    @DisplayName("판매 글 상태 목록")
    void getStatus() throws Exception{
        //given
        mockMvc.perform(get("/trade/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("names").isNotEmpty())
                .andDo(print())
                .andDo(document("status",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("names").description("판매 글 상태")
                        )
                ));

        //when

        //then

    }

}