package com.bmarket.securityservice.domain.trade.controller.RequestForm;

import com.bmarket.securityservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateTradeForm {

    private String title;
    private String context;
    private Integer price;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;

}
