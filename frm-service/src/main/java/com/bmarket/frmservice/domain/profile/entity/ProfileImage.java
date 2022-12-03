package com.bmarket.frmservice.domain.profile.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "PROFILE_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImage {
    @Id
    private String id;
    private String uploadImageName;
    private String storedImageName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 프로필 이미지 생성
     */
    @Builder(builderMethodName = "createProfileImage")
    public ProfileImage(String storedImageName) {
        this.storedImageName = storedImageName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    /**
     * 프로필 이미지 수정
     */
    public void updateProfileImage(String uploadImageName, String storedImageName) {
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
    }
}
