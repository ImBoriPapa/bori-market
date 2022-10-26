package com.bmarket.frmservice.domain.profile.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PROFILE_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImage {
    @Id
    private String id;
    private Long accountId;
    private String uploadImageName;
    private String storedImageName;

    /**
     * 프로필 이미지 생성
     * @param accountId
     * @param uploadImageName
     * @param storedImageName
     * @param size
     */
    @Builder(builderMethodName = "createProfileImage")
    public ProfileImage(Long accountId,String uploadImageName, String storedImageName, Long size) {
        this.accountId = accountId;
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;

    }

    /**
     * 프로필 이미지 삭제 요청시 업로드 이미지를 null, 이미지를 default 이미지로 변경
     * @param
     */
    public void deleteProfileImage(String defaultImage){
        this.uploadImageName = null;
        this.storedImageName = defaultImage;
    }

    /**
     * 프로필 이미지 수정
     * @param uploadImageName
     * @param storedImageName
     */
    public void updateProfileImage(String uploadImageName, String storedImageName) {
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
    }
}
