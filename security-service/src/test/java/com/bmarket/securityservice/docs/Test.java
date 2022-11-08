package com.bmarket.securityservice.docs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class TestDocs {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("test")
    void test() throws Exception {
        //given
        ResultActions resultActions = mockMvc.perform(get("/test"))
                .andExpect(status().isOk());

        //when

        //then
        resultActions.andDo(document("test-docs",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(

                        fieldWithPath("name").description("이름"),
                        fieldWithPath("age").description("나이"),
                        fieldWithPath("sex").description("성별")


                ))

        );


    }
}
