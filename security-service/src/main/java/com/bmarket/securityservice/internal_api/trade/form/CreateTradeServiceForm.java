package com.bmarket.securityservice.internal_api.trade.form;

import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.trade.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateTradeServiceForm {

    private Long accountId;
    private String title;
    private String context;
    private Integer price;
    private Address address;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
}
