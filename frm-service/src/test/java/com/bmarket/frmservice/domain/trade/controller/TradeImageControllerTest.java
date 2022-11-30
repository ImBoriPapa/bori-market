package com.bmarket.frmservice.domain.trade.controller;

import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Slf4j
class TradeImageControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    FileManager fileManager;

    @Value("${resource-path.trade-image-path}")
    private String path;

    @AfterEach
    void afterEach() {
        tradeImageRepository.deleteAll();
        fileManager.deleteAllInPath(path);
    }

    @Test
    @DisplayName("판매 이미지 생성")
    void postTradeImageTest() throws Exception {
        //given
        Long tradeId = 1L;
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "test-image".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "test-image".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "test-image".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("images", "image4.png", "image/png", "test-image".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("images", "image5.png", "image/png", "test-image".getBytes());
        //when

        //then
        mockMvc.perform(multipart("/frm/trade/{tradeId}/trade-image", tradeId)
                        .file(image1)
                        .file(image2)
                        .file(image3)
                        .file(image4)
                        .file(image5)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").value(tradeId))
                .andExpect(jsonPath("imagePath").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 이미지 생성 에러 테스트 이미지 파일이 없을 경우")
    void failPostTradeImage() throws Exception {
        //given
        Long tradeId = 1L;
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "test-image".getBytes());

        //when
        //then
        mockMvc.perform(multipart("/frm/trade/{tradeId}/trade-image", tradeId)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 이미지 생성 에러 테스트 이미지 파일이 10개 이상일 경우")
    void failPostTradeImage2() throws Exception {
        //given
        Long tradeId = 1L;
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "test-image".getBytes());

        //when
        //then
        mockMvc.perform(multipart("/frm/trade/{tradeId}/trade-image", tradeId)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .file(image1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("message").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 이미지 삭제 테스트")
    void deleteTradeImage() throws Exception {
        //given
        Long tradeId = 1L;
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());
        List<MultipartFile> files = List.of(file1, file2, file3);

        //when
        List<UploadFile> uploadFiles = fileManager.saveFile(path, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .tradeId(tradeId)
                .images(uploadFiles)
                .build();

        TradeImage save = tradeImageRepository.save(tradeImage);

        //then
        mockMvc.perform(delete("/frm/trade/{tradeId}/trade-image", tradeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").value(tradeId))
                .andExpect(jsonPath("imagePath").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("판매 이미지 수정 테스트")
    void putTradeImageTest() throws Exception {
        //given
        Long tradeId = 1L;
        MockMultipartFile file1 = new MockMultipartFile("test1", "test1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("test2", "test2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("test3", "test3.jpg", "jpg", "fsafaf".getBytes());
        List<MultipartFile> files = List.of(file1, file2, file3);

        List<UploadFile> uploadFiles = fileManager.saveFile(path, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .tradeId(tradeId)
                .images(uploadFiles).build();

        tradeImageRepository.save(tradeImage);
        //when
        MockMultipartFile newFile1 = new MockMultipartFile("images", "new1.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile newFile2 = new MockMultipartFile("images", "new2.jpg", "jpg", "fsafaf".getBytes());
        MockMultipartFile newFile3 = new MockMultipartFile("images", "new3.jpg", "jpg", "fsafaf".getBytes());


        //then
        MockMultipartHttpServletRequestBuilder builder = multipart("/frm/trade/{tradeId}/trade-image", tradeId);
        builder.with(new RequestPostProcessor() {

            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(newFile1)
                        .file(newFile2)
                        .file(newFile3)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("tradeId").value(tradeId))
                .andExpect(jsonPath("imagePath").isArray())
                .andDo(print());

    }
}