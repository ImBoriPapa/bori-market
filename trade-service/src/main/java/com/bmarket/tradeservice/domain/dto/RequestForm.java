package com.bmarket.tradeservice.domain.dto;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;
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
    private String title;
    private String context;
    private Integer price;
    private Address address;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
}
