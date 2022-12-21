package com.bmarket.tradeservice.api.requestForm;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.entity.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUpdateForm {

    private String title;
    private String context;
    private Integer price;
    private Category category;
    private Address address;
    private TradeType tradeType;
    private TradeStatus tradeStatus;
    private Boolean isOffer;
}
