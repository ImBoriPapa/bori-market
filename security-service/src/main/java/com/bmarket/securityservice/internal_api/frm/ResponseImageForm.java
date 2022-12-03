package com.bmarket.securityservice.internal_api.frm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseImageForm {

    private Boolean success;
    private String imageId;
    private String imagePath;
}
