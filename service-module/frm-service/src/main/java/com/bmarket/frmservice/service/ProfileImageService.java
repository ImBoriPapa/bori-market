package com.bmarket.frmservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    public void createImage(MultipartFile multipartFile);

}
