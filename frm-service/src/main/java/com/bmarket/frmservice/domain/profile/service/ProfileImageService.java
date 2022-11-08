package com.bmarket.frmservice.domain.profile.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

    public void createImage(MultipartFile multipartFile);

}
