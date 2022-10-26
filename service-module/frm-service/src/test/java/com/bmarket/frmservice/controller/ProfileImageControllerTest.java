package com.bmarket.frmservice.controller;

import com.bmarket.frmservice.domain.profile.controller.ProfileImageController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ProfileImageControllerTest {

    @Autowired
    ProfileImageController profileImageController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("프로필 이미지 생성 성공 테스트")
    void successCreate() throws Exception {
        //given

        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "something".getBytes());

        mockMvc.perform(multipart("/frm/profile")
                .file(image)
                .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("imagePath").isNotEmpty());

        //when

        //then

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