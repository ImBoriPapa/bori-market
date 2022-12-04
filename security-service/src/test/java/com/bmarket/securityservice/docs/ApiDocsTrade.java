package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.security.controller.LoginResult;
import com.bmarket.securityservice.domain.security.service.JwtService;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseTradeResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.TradeModifyResult;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeDeleteResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListDto;
import com.bmarket.securityservice.utils.testdata.TestDataProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
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


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ApiDocsTrade {

    @Autowired
    TestDataProvider testDataProvider;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtService jwtService;
    @Autowired
    ObjectMapper objectMapper;

    private MockWebServer mockWebServer;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
        mockWebServer.url("/internal/trade");

        String createResult = mockCreateResponse();

        String listResult = mockListResponse();

        String contentResult = mockContentResponse();

        String modifyResult = mockModifyResult();

        String deleteResult = mockDeleteResult();


        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/internal/trade")
                        && request.getMethod().equals("POST")) {
                    return new MockResponse()
                            .setBody(createResult)
                            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                } else if (request.getPath().equals("/internal/trade?size=20&lastIndex=5&category=ETC&isShare=true&isOffer=false&status=SALE&addressCode=1100&range=ONLY")
                        && request.getMethod().equals("GET")) {
                    return new MockResponse()
                            .setBody(listResult)
                            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                } else if (request.getPath().equals("/internal/trade/1")
                        && request.getMethod().equals("GET")) {
                    return new MockResponse()
                            .setBody(contentResult)
                            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                } else if (request.getPath().equals("/internal/trade/1") && request.getMethod().equals("PATCH")) {
                    return new MockResponse()
                            .setBody(modifyResult)
                            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                } else if (request.getPath().equals("/internal/trade/1") && request.getMethod().equals("DELETE")) {
                    return new MockResponse()
                            .setBody(deleteResult)
                            .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                }
                return new MockResponse().setResponseCode(404);
            }

        };

        mockWebServer.setDispatcher(dispatcher);

        testDataProvider.initAccount();
    }


    @AfterEach
    void afterEach() throws IOException {
        testDataProvider.clearAccount();
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("거래 생성")
    void createTrade() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        RequestTradeForm.CreateTradeForm form = RequestTradeForm.CreateTradeForm
                .builder()
                .title("제목")
                .context("내용")
                .price(12000).category(Category.DIGITAL).isShare(false).isOffer(false).build();

        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "data123".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "data123".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "data123".getBytes());
        MockMultipartFile images4 = new MockMultipartFile("images", "image4.png", "image/png", "data123".getBytes());
        MockMultipartFile images5 = new MockMultipartFile("images", "image5.png", "image/png", "data123".getBytes());
        MockMultipartFile json = new MockMultipartFile("form", "dd", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(form).getBytes());

        //then
        mockMvc.perform(multipart("/account/{accountId}/trade", loginResult.getAccountId())
                        .file(images1)
                        .file(images2)
                        .file(images3)
                        .file(images4)
                        .file(images5)
                        .file(json)
                        .headers(headers)
                ).andDo(print())
                .andDo(document("trade-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("accountId").description("계정 아이디")
                        ),
                        requestParts(partWithName("form").description("판매글 입력폼"),
                                partWithName("images").description("판매상품 이미지")),
                        requestPartFields("form",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("판매 글 제목"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("판매 글 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("상품 카테고리"),
                                fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("나눔 상품 여부"),
                                fieldWithPath("isOffer").type(JsonFieldType.BOOLEAN).description("가격 제안 여부")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.tradeId").type(JsonFieldType.NUMBER).description("생성된 판매글 아이디"),
                                fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("판매글 생성 시간"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열"),
                                fieldWithPath("result.links.[].rel").type(JsonFieldType.STRING).description("설명"),
                                fieldWithPath("result.links.[].href").type(JsonFieldType.STRING).description("링크")
                        )
                ));

    }

    @Test
    @DisplayName("판매글 리스트 조회 검색 조건 없이")
    void 판매글리스트조회() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());

        //then
        mockMvc.perform(get("/account/{accountId}/trade", loginResult.getAccountId())
                        .headers(headers)
                        .param("size", "20")
                        .param("lastIndex", "5")
                        .param("category", "ETC")
                        .param("isShare", "TRUE")
                        .param("isOffer", "FALSE")
                        .param("status", "SALE")
                        .param("addressCode", "1100")
                        .param("range", "ONLY")
                )
                .andDo(print())
                .andDo(document("trade-list-withoutCon",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("accountId").description("계정 아이디")
                        ),
                        requestParameters(
                                parameterWithName("size").description("요청할 목록 사이즈 기본값 20"),
                                parameterWithName("lastIndex").description("목록 마지막 인덱스 번호"),
                                parameterWithName("category").description("카테고리"),
                                parameterWithName("isShare").description("나눔 여부"),
                                parameterWithName("isOffer").description("가겨제안 여부"),
                                parameterWithName("status").description("판매글 상태"),
                                parameterWithName("addressCode").description("주소코드"),
                                parameterWithName("range").description("주소 검색 범위")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.size").type(JsonFieldType.NUMBER).description("조회된 수"),
                                fieldWithPath("result.hasNext").type(JsonFieldType.BOOLEAN).description("다음 목록 여부"),
                                fieldWithPath("result.tradeLists").type(JsonFieldType.ARRAY).description("판매 목록 결과"),
                                fieldWithPath("result.tradeLists.[].tradeId").type(JsonFieldType.NUMBER).description("판매글 아이디"),
                                fieldWithPath("result.tradeLists.[].title").type(JsonFieldType.STRING).description("판매글 제목"),
                                fieldWithPath("result.tradeLists.[].address").type(JsonFieldType.STRING).description("주소"),
                                fieldWithPath("result.tradeLists.[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("result.tradeLists.[].representativeImage").type(JsonFieldType.STRING).description("대표 이미지"),
                                fieldWithPath("result.tradeLists.[].createdAt").type(JsonFieldType.STRING).description("판매글 작성일"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열")

                        )

                ));
    }

    @Test
    @DisplayName("판매글 본문 조회")
    void 판매글본문조회() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        Long tradeId = 1L;
        //then
        mockMvc.perform(get("/trade/{tradeId}", tradeId)
                        .headers(headers)
                ).andDo(print())
                .andDo(document("trade-one",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedPathParameters(
                                parameterWithName("tradeId").description("판매글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.tradeId").type(JsonFieldType.NUMBER).description("판매 글 아이디"),
                                fieldWithPath("result.title").type(JsonFieldType.STRING).description("판매 글 제목"),
                                fieldWithPath("result.context").type(JsonFieldType.STRING).description("판매 글 내용"),
                                fieldWithPath("result.category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("result.imagePath").type(JsonFieldType.ARRAY).description("판매 상품 이미지 경로"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열")
                        )
                ));

    }

    @Test
    @DisplayName("수정")
    void modify() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        Long tradeId = 1L;

        RequestTradeForm.ModifyTradeForm modifyTradeForm = RequestTradeForm
                .ModifyTradeForm.builder()
                .title("제목바꾸기")
                .context("내용도바꿔")
                .price(200000)
                .category(Category.BEAUTY)
                .isShare(true)
                .isOffer(true)
                .build();
        MockMultipartFile images1 = new MockMultipartFile("images", "image1.png", "image/png", "dsadsa".getBytes());
        MockMultipartFile images2 = new MockMultipartFile("images", "image2.png", "image/png", "dsadsa".getBytes());
        MockMultipartFile images3 = new MockMultipartFile("images", "image3.png", "image/png", "dsadsa".getBytes());
        MockMultipartFile form = new MockMultipartFile("form",
                "modifyTradeForm",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(modifyTradeForm).getBytes());

        MockMultipartHttpServletRequestBuilder builder = multipart("/trade/{tradeId}", tradeId);
        builder.with(new RequestPostProcessor() {

            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        //then
        mockMvc.perform(builder
                        .file(images1)
                        .file(images2)
                        .file(images3)
                        .file(form)
                        .headers(headers)
                ).andDo(print())
                .andDo(document("trade-modify",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedPathParameters(
                                parameterWithName("tradeId").description("판매글 아이디")
                        ),
                        requestParts(
                                partWithName("form").description("수정 정보 입력폼"),
                                partWithName("images").description("수정할 이미지")
                        ),
                        requestPartFields("form",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 제목"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("수정할 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("수정할 가격"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리"),
                                fieldWithPath("isShare").type(JsonFieldType.BOOLEAN).description("나눔 여부"),
                                fieldWithPath("isOffer").type(JsonFieldType.BOOLEAN).description("가격제안 여부")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.tradeId").type(JsonFieldType.NUMBER).description("판매 글 아이디"),
                                fieldWithPath("result.updatedAt").type(JsonFieldType.STRING).description("수정 시간"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열")
                        )
                ));
    }

    @Test
    @DisplayName("삭제")
    void 판매글삭제() throws Exception {
        //given
        LoginResult loginResult = jwtService.loginProcessing("tester", "!@tester1234");

        HttpHeaders headers = new HttpHeaders();
        headers.set(CLIENT_ID, loginResult.getClientId());
        headers.set(AUTHORIZATION_HEADER, loginResult.getAccessToken());
        headers.set(REFRESH_HEADER, loginResult.getRefreshToken());
        //when
        Long tradeId = 1L;
        //then
        mockMvc.perform(delete("/trade/{tradeId}", tradeId)
                        .headers(headers)
                ).andDo(print())
                .andDo(document("trade-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedPathParameters(
                                parameterWithName("tradeId").description("판매글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("custom 응답 상태"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("custom 응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("result").type(JsonFieldType.OBJECT).description("응답 결과"),
                                fieldWithPath("result.isDelete").type(JsonFieldType.BOOLEAN).description("식제 여부"),
                                fieldWithPath("result.deletedAt").type(JsonFieldType.STRING).description("삭제 시간"),
                                fieldWithPath("result.links").type(JsonFieldType.ARRAY).description("링크 배열")
                        )
                ));

    }

    private String mockContentResponse() throws JsonProcessingException {
        TradeDetailResult contentsResult = TradeDetailResult.builder()
                .tradeId(1L)
                .title("한번쓴 치약팔아요")
                .context("상태 A급 치약입니다.")
                .category(Category.BEAUTY.name())
                .imagePath(
                        List.of(
                                "http://localhost8095/frm/image/fgdsgsdf242132f1.png",
                                "http://localhost8095/frm/image/fgdsgsdf242132f2.png",
                                "http://localhost8095/frm/image/fgdsgsdf242132f3.png",
                                "http://localhost8095/frm/image/fgdsgsdf242132f4.png")
                ).build();
        return objectMapper.writeValueAsString(contentsResult);
    }

    private String mockListResponse() throws JsonProcessingException {
        TradeListResult list1 = TradeListResult.builder()
                .tradeId(1L)
                .title("아이폰 팔아요")
                .townName("서울시 무슨구 무슨동")
                .price(1000000)
                .representativeImage("http://localhost:8095/frm/image/23414fqafafgg21.jpg")
                .createdAt(LocalDateTime.now()).build();

        TradeListResult list2 = TradeListResult.builder()
                .tradeId(2L)
                .title("겔럭시 팔아요")
                .townName("서울시 무슨구 무슨동")
                .price(900000)
                .representativeImage("http://localhost:8095/frm/image/glsgs24951n21j4kf.jpg")
                .createdAt(LocalDateTime.now()).build();

        TradeListResult list3 = TradeListResult.builder()
                .tradeId(3L)
                .title("붕어빵 팔아요")
                .townName("서울시 무슨구 무슨동")
                .price(90000)
                .representativeImage("http://localhost:8095/frm/image/jhjtrjt2314hshdh.jpg")
                .createdAt(LocalDateTime.now()).build();

        TradeListResult list4 = TradeListResult.builder()
                .tradeId(4L)
                .title("초콜릿 팔아요")
                .townName("인천시 무슨구 무슨동")
                .price(12000000)
                .representativeImage("http://localhost:8095/frm/image/fsafsaf2351251hdh.jpg")
                .createdAt(LocalDateTime.now()).build();

        TradeListResult list5 = TradeListResult.builder()
                .tradeId(5L)
                .title("가죽자켓 팔아요")
                .townName("인천시 무슨구 무슨동")
                .price(100)
                .representativeImage("http://localhost:8095/frm/image/gdsdsggsd1231312.jpg")
                .createdAt(LocalDateTime.now()).build();

        TradeListDto result = TradeListDto
                .builder()
                .size(5)
                .hasNext(true)
                .result(List.of(list1, list2, list3, list4, list5))
                .build();
        return objectMapper.writeValueAsString(result);
    }

    private String mockCreateResponse() throws JsonProcessingException {
        ResponseTradeResult result = ResponseTradeResult.builder()
                .tradeId(1L)
                .createdAt(LocalDateTime.now()).build();

        String createResult = objectMapper.writeValueAsString(result);
        return createResult;
    }

    private String mockModifyResult() throws JsonProcessingException {
        TradeModifyResult tradeModifyResult = new TradeModifyResult(1L, LocalDateTime.now());
        return objectMapper.writeValueAsString(tradeModifyResult);
    }

    private String mockDeleteResult() throws JsonProcessingException {
        TradeDeleteResult tradeDeleteResult = new TradeDeleteResult(true, LocalDateTime.now());

        String deleteResult = objectMapper.writeValueAsString(tradeDeleteResult);
        return deleteResult;
    }

}
