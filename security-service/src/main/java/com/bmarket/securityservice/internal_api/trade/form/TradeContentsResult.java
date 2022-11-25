package com.bmarket.securityservice.internal_api.trade.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TradeContentsResult {
    private Long tradeId;
    private String title;
    private String context;
    private String category;
    private List<String> imagePath;
}
