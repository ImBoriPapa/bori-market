package com.bmarket.securityservice.internal_api.frm;

import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Profile("test")
public class RequestFrmApiInterfaceImpl2 implements RequestFrmApiInterface {



    @Override
    public ResponseImageForm putProfileImage(String imageId, MultipartFile file) {

        //진짜 비즈니스 로직이 있는 구현체........

        return new ResponseImageForm(true, imageId, "realPath");
    }
}
