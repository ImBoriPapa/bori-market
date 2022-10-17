package com.bmarket.frmservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profileImage")
public class ProfileImage {

    private String id;
    private String uploadImageName;
    private String storedImageName;

}
