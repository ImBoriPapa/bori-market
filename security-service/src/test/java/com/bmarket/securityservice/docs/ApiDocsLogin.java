package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.security.controller.requestForm.RequestLoginForm;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
public class ApiDocsLogin {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestDataProvider testDataProvider;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach(){
        testDataProvider.initAccount();
    }

    @AfterEach
    void afterEach(){
        testDataProvider.clearAccount();
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception{
        String loginId = "tester";
        String password = "!@tester1234";
        RequestLoginForm loginForm = new RequestLoginForm(loginId,password);


        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginForm))
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 인덱스"),
                                fieldWithPath("result.loginAt").type(JsonFieldType.STRING).description("계정 생성시간"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("경로")
                        )

                        ));
    }

}
