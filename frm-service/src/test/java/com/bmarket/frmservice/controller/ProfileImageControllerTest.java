package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.domain.profile.controller.ProfileImageController;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void afterEach(){
        fileManager.deleteAllInPath(path);
        profileImageRepository.deleteAll();
    }

    @Test
    @DisplayName("프로필 이미지 저장 성공 테스트")
    void successCreate() throws Exception {
        //given
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "something".getBytes());
        //when
        Long accountId = 1L;
        //then
        mockMvc.perform(multipart("/frm/account/{accountId}/profile",accountId)
                        .file(image)
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("imagePath").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 이미지 생성 실패 테스트")
    void failCreate() throws Exception {
        //given

        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "something".getBytes());

        mockMvc.perform(multipart("/frm/profile")
                        .file(image)
                        .param("accountId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("imagePath").isNotEmpty());

        //when

        //then

    }

}