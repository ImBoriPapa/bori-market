package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.api.controller.AccountController;
import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class ApiDocs {


    @Autowired
    AccountController accountController;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("계정 생성")
    void createAccount() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("boripapa")
                .name("보리아빠")
                .password("papa1234")
                .nickname("papa")
                .contact("010-1111-2222")
                .email("papa@papa.com").build();
        String value = objectMapper.writeValueAsString(form);
        ResultActions resultActions = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isCreated())
                .andDo(document("account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인용 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("contact").type(JsonFieldType.STRING).description("contact")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.STRING).description("응답 내용"),
                                fieldWithPath("result.accountId").type(JsonFieldType.STRING).description("계정 인덱스"),
                                fieldWithPath("result.clientId").type(JsonFieldType.STRING).description("클라이언트 아이디"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("계정 생성시간"),
                                fieldWithPath("result._links.self.href").type(JsonFieldType.STRING).description("계정 생성시간")

                        )


                ));

        //when
        //then


    }
}
