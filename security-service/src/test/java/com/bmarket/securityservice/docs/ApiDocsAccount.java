package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.account.controller.AccountController;
import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.domain.testdata.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.MockResponse;
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

import java.io.IOException;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
/**
 * 실제 서비스 Mock 서버가 아닌 실제 서비스 연동 테스트 후 성공시 api docs 수정
 */
class ApiDocsAccount {


    @Autowired
    AccountController accountController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestData testData;
    @Autowired
    JwtService jwtService;

    public MockWebServer mockWebServer;


    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        MockResponse mockResponse = new MockResponse();
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody(objectMapper.writeValueAsString("http://localhost:8095//frm/profile/default.img"));
        mockWebServer.enqueue(mockResponse);
        testData.initAccount();
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
        testData.clearAccount();
    }

    @Test
    @DisplayName("계정 생성")
    void createAccount() throws Exception {

        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("imBoriPapa")
                .name("이아빠")
                .password("papa1234!@")
                .nickname("papa")
                .contact("01011113333")
                .email("papa@papa.com")
                .addressCode(1001)
                .city("서울시")
                .district("종로구")
                .town("종로동")
                .build();
        String value = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isCreated())
                .andDo(document("account",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인용 아이디")
                                        .attributes(key("size").value("영문자 or 영문자+숫자 6~15자리")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),

                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                        .attributes(key("size").value("2~10자리")),

                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        .attributes(key("size").value("숫자,영문자,특수문자를 포함한 8~16자리")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("contact").type(JsonFieldType.STRING).description("연락처")
                                        .attributes(key("size").value("'-'을 제외한 숫자")),
                                fieldWithPath("addressCode").type(JsonFieldType.NUMBER).description("주소 식별코드")
                                        .attributes(key("size").value("정수 1000에서 3000 사이")),

                                fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
                                fieldWithPath("district").type(JsonFieldType.STRING).description("구"),
                                fieldWithPath("town").type(JsonFieldType.STRING).description("동네")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 인덱스"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("계정 생성시간"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("경로")
                        )
                ));

    }

    @Test
    @DisplayName("계정 단건 조회 ")
    void getOneAccount() throws Exception {

        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        mockMvc.perform(get("/account/1")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getOneAccount",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디(인덱스)"),
                                fieldWithPath("result.loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("result.name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("result.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("result.contact").type(JsonFieldType.STRING).description("연락처"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("계정 생성일"),
                                fieldWithPath("result.updatedAt").type(JsonFieldType.STRING).description("계정 정보 수정일"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 정보"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));
    }

    @Test
    @DisplayName("계정 비밀번호 변경")
    void putPassword() throws Exception {
        String password = "!@tester1234";
        String newPassword = "!@newPassword1234";
        RequestAccountForm.UpdatePasswordForm passwordForm = new RequestAccountForm.UpdatePasswordForm(password, newPassword);

        String value = objectMapper.writeValueAsString(passwordForm);

        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        mockMvc.perform(put("/account/1/password")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())

                .andDo(document("accountPutPassword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("기존 비밀번호")
                                        .attributes(key("size").value("영문자 or 영문자+숫자 6~15자리")),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호")
                                        .attributes(key("size").value("영문자 or 영문자+숫자 6~15자리"))
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 인덱스"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("경로")
                        )
                ));

    }

    @Test
    @DisplayName("계정 이메일 변경")
    void putEmail() throws Exception {


        RequestAccountForm.UpdateEmailForm emailForm = new RequestAccountForm.UpdateEmailForm("newEmail@email.com");

        String value = objectMapper.writeValueAsString(emailForm);

        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        mockMvc.perform(put("/account/1/email")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())

                .andDo(document("accountPutEmail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("새 이메일")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 인덱스"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("경로")
                        )
                ));


    }

    @Test
    @DisplayName("계정 생성 에러")
    void createAccountError() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
                .loginId("")
                .name("이아빠")
                .password("papa1234!@")
                .nickname("papa")
                .contact("01012341234")
                .email("papa@papa.com")
                .addressCode(1001)
                .city("서울시")
                .district("종로구")
                .town("종로동")
                .build();
        String value = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isBadRequest())
                .andDo(document("account-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인용 아이디")
                                        .attributes(key("size").value("영문자 or 영문자+숫자 6~15자리")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름"),

                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                        .attributes(key("size").value("2~10자리")),

                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        .attributes(key("size").value("숫자,영문자,특수문자를 포함한 8~16자리")),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("contact").type(JsonFieldType.STRING).description("연락처")
                                        .attributes(key("size").value("'-'을 제외한 숫자")),
                                fieldWithPath("addressCode").type(JsonFieldType.NUMBER).description("주소 식별코드")
                                        .attributes(key("size").value("정수 1000에서 3000 사이")),

                                fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
                                fieldWithPath("district").type(JsonFieldType.STRING).description("구"),
                                fieldWithPath("town").type(JsonFieldType.STRING).description("동네")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("errorType").type(JsonFieldType.STRING).description("응답 타입"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.ARRAY).description("응답 메시지")
                        )
                ));

    }
}
