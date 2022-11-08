package com.bmarket.securityservice.api.trade.controller.RequestForm;

import com.bmarket.securityservice.api.trade.entity.Category;
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
