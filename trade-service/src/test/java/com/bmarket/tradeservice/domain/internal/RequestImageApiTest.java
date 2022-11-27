package com.bmarket.tradeservice.domain.internal;

import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
class RequestImageApiTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RequestImageApi requestImageApi;

    private MockWebServer mockWebServer;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String imagePath4;
    String imagePath5;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/trade");

        imagePath1 = "imagePath1.png";
        imagePath2 = "imagePath2.png";
        imagePath3 = "imagePath3.png";
        imagePath4 = "imagePath4.png";
        imagePath5 = "imagePath5.png";

        ResponseImageDto imageDto = new ResponseImageDto(List.of(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5));
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(objectMapper.writeValueAsString(imageDto));
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        mockWebServer.enqueue(mockResponse);

    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("이미지 저장 API 테스트")
    void getImageTest() throws Exception {
        //given
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        List<MultipartFile> files = List.of(image1, image2, image3, image4, image5);
        //when
        ResponseImageDto imagePath = requestImageApi.getImagePath(1L, files);
        //then
        assertThat(imagePath.getImagePath().get(0)).isEqualTo(imagePath1);
        assertThat(imagePath.getImagePath().get(1)).isEqualTo(imagePath2);
        assertThat(imagePath.getImagePath().get(2)).isEqualTo(imagePath3);
        assertThat(imagePath.getImagePath().get(3)).isEqualTo(imagePath4);
        assertThat(imagePath.getImagePath().get(4)).isEqualTo(imagePath5);
    }

}