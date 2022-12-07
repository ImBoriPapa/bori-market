package com.bmarket.securityservice.internal_api.frm;


import org.springframework.web.multipart.MultipartFile;

public interface RequestFrmApiInterface {

    public ResponseImageForm putProfileImage(String imageId, MultipartFile file);
}
