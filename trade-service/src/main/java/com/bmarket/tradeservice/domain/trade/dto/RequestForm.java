package com.bmarket.tradeservice.domain.trade.dto;

import com.bmarket.tradeservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RequestForm {
    private Long accountId;
    private String nickname;
    private String profileImage;
    private String title;
    private String context;
    private Integer price;
    private Integer addressCode;
    private String townName;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
}
