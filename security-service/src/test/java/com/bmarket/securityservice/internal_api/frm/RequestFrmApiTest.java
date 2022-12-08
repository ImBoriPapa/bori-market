package com.bmarket.securityservice.internal_api.frm;

import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.UUID;


@SpringBootTest
@ActiveProfiles("local")
class RequestFrmApiTest {

    @Autowired
    ObjectMapper objectMapper;
    private MockWebServer mockWebServer;
    private Dispatcher dispatcher;

    @BeforeEach
    void beforeEach() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);

        MockResponse postResponse = new MockResponse();
        ResponseImageForm postResult = new ResponseImageForm(true, UUID.randomUUID().toString(),"기본이미지 경로");
        postResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        postResponse.setBody(objectMapper.writeValueAsString(postResult));

        MockResponse putResponse = new MockResponse();
        ResponseImageForm putResult = new ResponseImageForm(true, UUID.randomUUID().toString(),"변경된 이미지 경로");
        putResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        putResponse.setBody(objectMapper.writeValueAsString(putResult));

        MockResponse deleteResponse = new MockResponse();
        ResponseImageForm deleteResult = new ResponseImageForm(true, null,null);
        deleteResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        deleteResponse.setBody(objectMapper.writeValueAsString(deleteResult));

        dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {

                if (request.getMethod().equals("POST")) {
                    return postResponse;
                }
                
                if(request.getMethod().equals("PUT")){
                    return putResponse;
                }

                if(request.getMethod().equals("DELETE")){
                    return deleteResponse;
                }

                return new MockResponse().setResponseCode(400);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
    }
    @AfterEach
    void afterEach() throws IOException {
        dispatcher.shutdown();
        mockWebServer.shutdown();
    }

    @Autowired
    RequestFrmApi requestFrmApi;

    @Test
    @DisplayName("프로필 이미지 생성 요청 테스트")
    void getProfileImageTest() throws Exception{
        //given
        
        //when
        ResponseImageForm profileImage = requestFrmApi.postProfileImage();
        //then
        Assertions.assertThat(profileImage.getSuccess()).isTrue();
        Assertions.assertThat(profileImage.getImageId()).isNotEmpty();
        Assertions.assertThat(profileImage.getImagePath()).isNotEmpty();

    }
    
    @Test
    @DisplayName("프로필 이미지 수정 요청 테스트")        
    void putProfileImageTest() throws Exception{
        //given
        String imageId = UUID.randomUUID().toString();
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "sfafa".getBytes());
        //when
        ResponseImageForm responseImageForm = requestFrmApi.putProfileImage(imageId, image);
        //then
        Assertions.assertThat(responseImageForm.getSuccess()).isTrue();
        Assertions.assertThat(responseImageForm.getImageId()).isNotEmpty();
        Assertions.assertThat(responseImageForm.getImagePath()).isNotEmpty();
    
    }

    @Test
    @DisplayName("프로필 이미지 삭제 요청 테스트")
    void deleteProfileImageTest() throws Exception{
        //given
        String imageId = UUID.randomUUID().toString();
        //when
        ResponseImageForm responseImageForm = requestFrmApi.deleteProfileImage(imageId);
        //then
        Assertions.assertThat(responseImageForm.getSuccess()).isTrue();
        Assertions.assertThat(responseImageForm.getImageId()).isNull();
        Assertions.assertThat(responseImageForm.getImagePath()).isNull();

    }

}