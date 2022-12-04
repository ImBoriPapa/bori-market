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
public class TradeDetailResult {
    private Long tradeId;
    private Long accountId;
    private String title;
    private String context;
    private String category;
    private Boolean isShare;
    private Boolean isOffer;
    private List<String> imagePath;
}
