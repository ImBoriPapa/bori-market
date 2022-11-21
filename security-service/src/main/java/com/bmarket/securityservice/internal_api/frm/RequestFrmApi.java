package com.bmarket.securityservice.internal_api.frm;

import org.springframework.web.multipart.MultipartFile;

public interface RequestFrmApi {

    public String getDefaultProfileImage();

    public String requestSaveImage(Long accountId, MultipartFile file);

    public String requestDeleteImage(Long accountId);

}
