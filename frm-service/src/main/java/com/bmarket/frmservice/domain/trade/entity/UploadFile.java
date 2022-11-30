package com.bmarket.frmservice.domain.trade.entity;

import lombok.Getter;

@Getter
public class UploadFile {

    private String uploadImageName;
    private String storedImageName;

    public UploadFile(String uploadImageName, String storedImageName) {
        this.uploadImageName = uploadImageName;
        this.storedImageName = storedImageName;
    }
}
