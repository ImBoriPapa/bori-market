package com.bmarket.tradeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseImageDto {

    private Boolean success;
    private String imageId;
    private List<String> imagePath;
}
