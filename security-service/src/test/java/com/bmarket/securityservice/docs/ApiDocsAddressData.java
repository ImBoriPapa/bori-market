package com.bmarket.securityservice.docs;

import com.bmarket.securityservice.domain.address.AddressResult;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ApiDocsAddressData {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private MockWebServer mockGetListResult;

    @BeforeEach
    void beforeEach() throws IOException {
        List<AddressResult> results = List.of(
                new AddressResult(1052, "서울시", "강북구", "우이동"),
                new AddressResult(1064, "서울시", "강서구", "우장산동"),
                new AddressResult(1421, "서울시", "중랑구", "망우본동"),
                new AddressResult(1422, "서울시", "중랑구", "망우제3동"));
        mockGetListResult = new MockWebServer();
        mockGetListResult.start(8085);
        mockGetListResult.url("/internal/address")
                .queryParameter("town");
        MockResponse mockResponse2 = new MockResponse();
        mockResponse2.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse2.setBody(objectMapper
                .writeValueAsString(results));
        mockGetListResult.enqueue(mockResponse2);
    }

    @AfterEach
    void afterEach() throws IOException {

        mockGetListResult.shutdown();
    }


    @Test
    @DisplayName("주소 검색 서비스")
    void searchAddress() throws Exception {

        String townName = "우장산동";
        mockMvc.perform(get("/address?town=우").characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("town").description("동네 이름으로 검색")
                        ), responseFields(
                                fieldWithPath("[].addressCode").type(JsonFieldType.NUMBER).description("주소 코드"),
                                fieldWithPath("[].city").type(JsonFieldType.STRING).description("도시명"),
                                fieldWithPath("[].district").type(JsonFieldType.STRING).description("지역명"),
                                fieldWithPath("[].town").type(JsonFieldType.STRING).description("동네명")
                        )
                ));

    }
}
