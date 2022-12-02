package com.bmarket.tradeservice.domain.internal;

import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.exception.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
class RequestImageApiTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RequestImageApi requestImageApi;
    private MockWebServer mockWebServer;
    private String imageId = UUID.randomUUID().toString();
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String imagePath4;
    String imagePath5;

    @BeforeEach
    void beforeEach() throws IOException {
        imagePath1 = "imagePath1.png";
        imagePath2 = "imagePath2.png";
        imagePath3 = "imagePath3.png";
        imagePath4 = "imagePath4.png";
        imagePath5 = "imagePath5.png";


        ResponseImageDto post = new ResponseImageDto(true, imageId, List.of(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5));
        ResponseImageDto delete = new ResponseImageDto(false, imageId, new ArrayList<>());
        ResponseImageDto put = new ResponseImageDto(true, imageId, List.of(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5));

        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);

        MockResponse postResponse = new MockResponse();
        postResponse.setBody(objectMapper.writeValueAsString(post));
        postResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        MockResponse deleteResponse = new MockResponse();
        deleteResponse.setBody(objectMapper.writeValueAsString(delete));
        deleteResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        MockResponse putResponse = new MockResponse();
        putResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        putResponse.setBody(objectMapper.writeValueAsString(put));

        MockResponse badRequestResponse = new MockResponse();
        badRequestResponse.setResponseCode(400);


        // TODO: 2022/12/02 요청에 파일이 없을 경우 어떻게 해야할지 고민해보자
        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {

                if (request.getMethod().equals("POST") && request.getPath().equals("/frm/trade-image")) {
                    log.info("successMockResponse");
                    return postResponse;
                }

                if (request.getMethod().equals("DELETE") && request.getPath().equals("/frm/trade-image/" + imageId)) {
                    log.info("deleteResponse");
                    return deleteResponse;
                }

                if(request.getMethod().equals("PUT") && request.getPath().equals("/frm/trade-image/" + imageId)) {
                    log.info("putResponse");
                    return putResponse;
                }
                // TODO: 2022/12/02 요청에 파일이 없을 경우 어떻게 해야할지 고민해보자
                if (request.getMethod().equals("POST") && request.getPath().equals("/frm/trade-image/??")) {
                    log.info("badRequestResponse");
                    return badRequestResponse;
                }

                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("이미지 저장 API 테스트")
    void getImageTest() throws Exception {
        //given
        List<MultipartFile> files = mockImages();
        //when
        ResponseImageDto responseImageDto = requestImageApi.postTradeImages(files);
        //then
        assertThat(responseImageDto.getSuccess()).isTrue();
        assertThat(responseImageDto.getImageId()).isNotNull();
        assertThat(responseImageDto.getImagePath().get(0)).isEqualTo(imagePath1);
        assertThat(responseImageDto.getImagePath().get(1)).isEqualTo(imagePath2);
        assertThat(responseImageDto.getImagePath().get(2)).isEqualTo(imagePath3);
        assertThat(responseImageDto.getImagePath().get(3)).isEqualTo(imagePath4);
        assertThat(responseImageDto.getImagePath().get(4)).isEqualTo(imagePath5);
    }

    @Test
    @DisplayName("이미지 삭제 API 테스트")
    void deleteImageTest() throws Exception {
        //given
        //when
        ResponseImageDto responseImageDto = requestImageApi.deleteTradeImages(imageId);

        //then
        assertThat(responseImageDto.getSuccess()).isFalse();
        assertThat(responseImageDto.getImageId()).isEqualTo(imageId);
        assertThat(responseImageDto.getImagePath()).isEmpty();

    }

    @Test
    @DisplayName("이밎 수정 API 테스트")
    void putImagesTest() throws Exception{
        //given
        List<MultipartFile> files = mockImages();
        //when
        ResponseImageDto responseImageDto = requestImageApi.updateTradeImages(imageId, files);
        //then
        assertThat(responseImageDto.getSuccess()).isTrue();
    }

    @Test
    @DisplayName("이미지 저장 API BAD REQUEST 응답 테스트")
    void badRequestResponse() throws Exception {
        //given
        List<MultipartFile> files = new ArrayList<>();
        //when
        //then
        assertThatThrownBy(() -> requestImageApi.postTradeImages(files))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ERROR_4XX_TO_FRM.getMessage());
    }


    @NotNull
    private static List<MultipartFile> mockImages() {
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("images", "image1.png", "image/png", "image".getBytes());
        List<MultipartFile> files = List.of(image1, image2, image3, image4, image5);
        return files;
    }

}