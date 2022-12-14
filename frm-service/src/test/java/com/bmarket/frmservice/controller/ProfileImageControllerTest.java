package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.controller.profile.ProfileImageController;
import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static com.bmarket.frmservice.utils.AccessUrl.DEFAULT_IMAGE_NAME;
import static com.bmarket.frmservice.utils.AccessUrl.DEFAULT_PROFILE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        //when

        //then
        mockMvc.perform(post("/internal/frm/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("imageId").exists())
                .andExpect(jsonPath("imagePath").isNotEmpty())
                .andDo(print());
    }



    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void putProfileImageTest() throws Exception {
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        MockMultipartFile newImage = new MockMultipartFile("image", "newImage.png", "image/png", "image".getBytes());
        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        ProfileImage findProfile = profileImageRepository.findById(save.getId()).orElseThrow(() -> new IllegalArgumentException("프로필 이미지를 찾을 수 없습니다."));

        MockMultipartHttpServletRequestBuilder builder = multipart("/internal/frm/profile/{imageId}", findProfile.getId());
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
                .andExpect(jsonPath("imageId").value(findProfile.getId()))
                .andExpect(jsonPath("imagePath").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("이미지 수정 테스트 수정할 이미지를 보내지 않았을때")
    void updateImageNoContainImage() throws Exception{
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        //then
        mockMvc.perform(put("/internal/frm/profile/{imageId}",save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("imageId").value(save.getId()))
                .andExpect(jsonPath("imagePath").value(DEFAULT_PROFILE_URL +DEFAULT_IMAGE_NAME))
                .andDo(print());

    }

    @Test
    @DisplayName("프로필 이미지 삭제 테스트")
    void deleteProfileImageTest() throws Exception{
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .storedImageName(DEFAULT_IMAGE_NAME).build();

        //when
        ProfileImage save = profileImageRepository.save(profileImage);

        //then
        mockMvc.perform(delete("/internal/frm/profile/{imageId}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("imageId").doesNotExist())
                .andExpect(jsonPath("imagePath").doesNotExist())
                .andDo(print());
    }

}