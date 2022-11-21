package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.domain.testdata.TestData;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
public class ApiDocsProfile {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtService jwtService;

    @Autowired
    TestData testData;

    public MockWebServer mockWebServer;

    @BeforeEach
    void beforeEach() {
        testData.initAccount();
    }

    @AfterEach
    void afterEach() {
        testData.clearAccount();
    }

    @Test
    @DisplayName("프로필 단건 조회")
    void getProfile() throws Exception {

        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        mockMvc.perform(get("/profile/1")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("getProfile",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디(인덱스)"),
                                fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("result.profileImage").type(JsonFieldType.STRING).description("프로필 이미지 정적 리소스 경로"),
                                fieldWithPath("result.addressRange").type(JsonFieldType.STRING).description("주소 검색 범위"),
                                fieldWithPath("result.addressRangeEx").type(JsonFieldType.STRING).description("주소 검색 범위 설명"),
                                fieldWithPath("result.fullAddress").type(JsonFieldType.STRING).description("현재 저장된 주소"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열")
                        )
                ));


    }

}
