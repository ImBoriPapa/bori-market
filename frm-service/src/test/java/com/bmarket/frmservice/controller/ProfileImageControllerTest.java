package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.domain.profile.controller.ProfileImageController;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.IOException;

import static com.bmarket.frmservice.utils.Patterns.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class ProfileImageControllerTest {

    @Autowired
    ProfileImageController profileImageController;
    @Autowired
    ProfileImageRepository profileImageRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FileManager fileManager;
    @Value("${resource-path.profile-image-path}")
    String path;

    @AfterEach
    void afterEach() {
        fileManager.deleteAllInPath(path);
        profileImageRepository.deleteAll();
    }

    @Test
    @DisplayName("프로필 이미지 저장 성공 테스트")
    void successSave() throws Exception {
        //given
        Long accountId = 1L;
        //when

        //then
        mockMvc.perform(post("/internal/frm/account/{accountId}/profile", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("imagePath").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 이미지 저장 실패 테스트 계정 아이디 중복")
    void failSave() throws Exception {
        //given
        ProfileImage image = ProfileImage.createProfileImage()
                .accountId(1L)
                .build();
        //when
        ProfileImage savedProfile = profileImageRepository.save(image);

        //then
        mockMvc.perform(post("/internal/frm/account/{accountId}/profile", savedProfile.getAccountId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void putProfileImageTest() throws Exception {
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(1L)
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        MockMultipartFile newImage = new MockMultipartFile("image", "newImage.png", "image/png", "image".getBytes());
        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        MockMultipartHttpServletRequestBuilder builder = multipart("/internal/frm/account/{accountId}/profile", save.getAccountId());
        builder.with(new RequestPostProcessor() {

            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        //then
        mockMvc.perform(builder
                        .file(newImage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("accountId").value(save.getAccountId()))
                .andExpect(jsonPath("imagePath").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("이미지 수정 테스트 수정할 이미지를 보내지 않았을때")
    void updateImageNoContainImage() throws Exception{
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(1L)
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        //then
        mockMvc.perform(put("/internal/frm/account/{accountId}/profile",save.getAccountId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("accountId").value(save.getAccountId()))
                .andExpect(jsonPath("imagePath").value(SEARCH_DEFAULT_PATTERN+DEFAULT_IMAGE_NAME))
                .andDo(print());

    }

    @Test
    @DisplayName("프로필 이미지 삭제 테스트")
    void deleteProfileImageTest() throws Exception{
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(1L)
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        //then
        mockMvc.perform(delete("/internal/frm/account/{accountId}/profile", save.getAccountId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("accountId").value(save.getAccountId()))
                .andExpect(jsonPath("imagePath").doesNotExist())
                .andDo(print());
    }

}