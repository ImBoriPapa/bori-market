package com.bmarket.frmservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PROFILE_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImage {

    private String id;
    private String uploadImageName;
    private String storedImageName;
    private Long size;

    @Builder(builderMethodName = "createProfileImage")
    public ProfileImage(String uploadImageName, String storedImageName, Long size) {
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
        this.size = size;
    }
}
