package com.bmarket.securityservice.internal_api.frm;

import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Profile("local")
public class RequestFrmApiInterfaceImpl implements RequestFrmApiInterface {

    private String imagePath = "http://loaclhost:8080/frm/file/" + UUID.randomUUID() + ".png";

    @Override
    public ResponseImageForm putProfileImage(String imageId, MultipartFile file) {
        return new ResponseImageForm(true, imageId, imagePath);
    }
}
