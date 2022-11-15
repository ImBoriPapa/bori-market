package com.bmarket.securityservice.domain.trade.service.form;

import com.bmarket.securityservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RequestTradeForm {
    private Long accountId;
    private String nickname;
    private String title;
    private String context;
    private Integer price;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
}
