package com.bmarket.securityservice.api.trade.service.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TradeDetailResult {
    private Long tradeId;
    private String nickName;
    private String title;
    private String context;
    private String category;
    private String townName;
    private List<String> imagePath;
}
