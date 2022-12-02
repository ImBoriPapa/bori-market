package com.bmarket.frmservice.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTradeImage {

    private Boolean success;
    private String imageId;
    private List<String> imagePath;

}
