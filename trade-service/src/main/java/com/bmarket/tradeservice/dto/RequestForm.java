package com.bmarket.tradeservice.dto;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RequestForm {

    private String memberId;
    private String title;
    private String context;
    private Integer price;
    private Address address;
    private Category category;
    private Boolean isOffer;
    private TradeType tradeType;
}
