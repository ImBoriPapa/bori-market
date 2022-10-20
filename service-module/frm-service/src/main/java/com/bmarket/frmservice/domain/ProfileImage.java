package com.bmarket.frmservice.domain;

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


    @Builder(builderMethodName = "createProfileImage")
    public ProfileImage(Long accountId,String uploadImageName, String storedImageName, Long size) {
        this.accountId = accountId;
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;

    }

    public void deleteProfileImage(String storedImageName){
        this.uploadImageName = null;
        this.storedImageName = storedImageName;
    }

    public void updateProfileImage(String uploadImageName, String storedImageName) {
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
    }
}
