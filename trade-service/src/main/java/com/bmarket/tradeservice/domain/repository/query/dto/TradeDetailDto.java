package com.bmarket.tradeservice.domain.repository.query.dto;

import com.bmarket.tradeservice.domain.entity.Address;
import com.bmarket.tradeservice.domain.entity.Category;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TradeDetailDto {
    private Long tradeId;
    private Long accountId;
    private String title;
    private String context;
    private Category category;
    private Address address;
    private Boolean isShare;
    private Boolean isOffer;
    private List<String> imagePath;

    @QueryProjection
    public TradeDetailDto(Long tradeId, Long accountId, String title, String context, Category category, Address address, Boolean isShare, Boolean isOffer) {
        this.tradeId = tradeId;
        this.accountId = accountId;
        this.title = title;
        this.context = context;
        this.category = category;
        this.address = address;
        this.isShare = isShare;
        this.isOffer = isOffer;
    }

    public void addImagePath(List<String> imagePath){
        this.imagePath = imagePath;
    }
}
