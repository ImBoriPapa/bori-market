package com.bmarket.tradeservice.domain.repository.query.dto;

import com.bmarket.tradeservice.domain.entity.Category;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor

@Getter
public class TradeDetailDto {
    private Long tradeId;
    private String title;
    private String context;
    private Category category;
    private String address;
    private List<String> imagePath;
    @QueryProjection
    public TradeDetailDto(Long tradeId, String title, String context, Category category, String address) {
        this.tradeId = tradeId;
        this.title = title;
        this.context = context;
        this.category = category;
        this.address = address;
    }

    public void addImagePath(List<String> imagePath){
        this.imagePath = imagePath;
    }
}
