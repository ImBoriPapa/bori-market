package com.bmarket.tradeservice.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseImageDto {

    private List<String> imagePath;
}
