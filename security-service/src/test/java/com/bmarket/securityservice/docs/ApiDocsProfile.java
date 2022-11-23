package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.controller.RequestProfileForm;
import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    ObjectMapper objectMapper;
    @Autowired
    TestDataProvider testDataProvider;

    public MockWebServer mockTradeService;
    public MockWebServer mockFrmService;

    @BeforeEach
    void beforeEach() throws IOException {

        mockTradeService = new MockWebServer();
        mockTradeService.start(8081);
        mockTradeService.url("/internal/trade/account/1/nickname");
        MockResponse mockTradeResponse = new MockResponse();

        mockTradeResponse.setBody(objectMapper.writeValueAsString("ok"));
        mockTradeResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockTradeService.enqueue(mockTradeResponse);

        mockFrmService = new MockWebServer();
        mockFrmService.start(8095);
        mockFrmService.url("/frm/profile/1");

        MockResponse mockFrmResponse = new MockResponse();
        mockFrmResponse.setBody("http://localhost:8095/" + UUID.randomUUID() + ".png");
        mockFrmService.enqueue(mockTradeResponse);

        testDataProvider.initAccount();
    }

    @AfterEach
    void afterEach() throws IOException {
        mockTradeService.shutdown();
        mockFrmService.shutdown();
        testDataProvider.clearAccount();
    }

    @Test
    @DisplayName("프로필 단건 조회")
    void getProfile() throws Exception {

        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        mockMvc.perform(get("/account/1/profile")
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
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));


    }

    // TODO: 2022/11/21 문서작성
    @Test
    @DisplayName("닉네임 수정")
    void updateAddressNickname() throws Exception {
        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        mockMvc.perform(patch("/account/1/profile/nickname")
                        .content(objectMapper.writeValueAsString(new RequestProfileForm.UpdateNickname("새로운닉네임")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("profile-update-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("nickname").type(JsonFieldType.STRING).description("변경할 닉네임")),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));

    }

    @Test
    @DisplayName("주소 변경")
    void updateAddress() throws Exception {
        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        mockMvc.perform(patch("/account/1/profile/address")
                        .content(objectMapper.writeValueAsString(new RequestProfileForm.UpdateAddress(1002, "서울시", "여의도", "여의도동")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("profile-update-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addressCode").type(JsonFieldType.NUMBER).description("변경할 addressCode"),
                                fieldWithPath("city").type(JsonFieldType.STRING).description("변경할 도시"),
                                fieldWithPath("district").type(JsonFieldType.STRING).description("변경할 구"),
                                fieldWithPath("town").type(JsonFieldType.STRING).description("변경할 동네")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));
    }

    @Test
    @DisplayName("주소 검색 범위 변경")
    void updateRange() throws Exception {
        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        mockMvc.perform(patch("/account/1/profile/range")
                        .content(objectMapper.writeValueAsString(new RequestProfileForm.UpdateRange(AddressRange.TEN)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("profile-update-range",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("addressRange").type(JsonFieldType.STRING).description("변경할 주소 검색 범위")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    void updateImage() throws Exception {
        LoginResult tester = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(CLIENT_ID, tester.getClientId());
        httpHeaders.set(AUTHORIZATION_HEADER, tester.getAccessToken());
        httpHeaders.set(REFRESH_HEADER, tester.getRefreshToken());

        MockMultipartHttpServletRequestBuilder builder = multipart("/account/1/profile/image");
        builder.with(new RequestPostProcessor() {

            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        mockMvc.perform(builder
                        .file(new MockMultipartFile("image", "newImage", "image/png", "dasdada".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("profile-update-image",
                        preprocessResponse(prettyPrint()),
                        requestParts(partWithName("image").description("변경할 프로필 이미지")),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.accountId").type(JsonFieldType.NUMBER).description("계정 아이디"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("링크 설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));
    }

}
