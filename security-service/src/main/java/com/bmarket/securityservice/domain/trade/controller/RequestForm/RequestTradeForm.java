package com.bmarket.securityservice.domain.trade.controller.RequestForm;

import com.bmarket.securityservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RequestTradeForm {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateTradeForm{
        private String title;
        private String context;
        private Integer price;
        private Category category;
        private Boolean isShare;
        private Boolean isOffer;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ModifyTradeForm{
        private String title;
        private String context;
        private Integer price;
        private Category category;
        private Boolean isShare;
        private Boolean isOffer;
    }

}
