package com.bmarket.frmservice.service;

import com.bmarket.frmservice.domain.ProfileImage;
import com.bmarket.frmservice.repository.ProfileImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfileImageServiceV1Test {

    @Autowired ProfileImageServiceV1 profileImageServiceV1;


    @Test
    @DisplayName("이미지 저장 성공 테스트")
    void successSave() throws Exception{
        //given
        long accountId = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "test-image".getBytes(StandardCharsets.UTF_8) );

        //when
        String save = profileImageServiceV1.save(accountId, image);
        ProfileImage findProfile = profileImageServiceV1.findByAccountId(accountId);
        //then
        Assertions.assertThat(findProfile.getUploadImageName()).isEqualTo(image.getOriginalFilename());
    }

}