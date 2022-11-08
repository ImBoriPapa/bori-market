package com.bmarket.frmservice.domain.trade.entity;

import lombok.Getter;

@Getter
public class UploadFile {

    private String uploadName;
    private String storedName;

    public UploadFile(String uploadName, String storedName) {
        this.uploadName = uploadName;
        this.storedName = storedName;
    }
}
