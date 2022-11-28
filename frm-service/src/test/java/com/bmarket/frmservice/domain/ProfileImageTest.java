package com.bmarket.frmservice.domain;

import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import com.bmarket.frmservice.domain.profile.repository.ProfileImageRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;


import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("local")
class ProfileImageTest {

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Test
    @DisplayName("프로파일 이미지 생성 테스트")
    void createProfileImageTest() throws Exception {
        //given
        ProfileImage profileImage = ProfileImage.createProfileImage()
                .accountId(1L)
                .uploadImageName("upload.jpg")
                .storedImageName("stored.jpg")
                .build();
        //when
        ProfileImage saveImage = profileImageRepository.save(profileImage);
        Optional<ProfileImage> optionalProfileImage = profileImageRepository.findById(saveImage.getId());
        ProfileImage findImage = optionalProfileImage.orElseThrow(() -> new IllegalArgumentException("프로필 이미지를 찾을수 없습니다."));
        //then
        assertThat(findImage.getId()).isEqualTo(saveImage.getId());
        assertThat(findImage.getStoredImageName()).isEqualTo(saveImage.getStoredImageName());
        assertThat(findImage.getUploadImageName()).isEqualTo(saveImage.getUploadImageName());

    }

}