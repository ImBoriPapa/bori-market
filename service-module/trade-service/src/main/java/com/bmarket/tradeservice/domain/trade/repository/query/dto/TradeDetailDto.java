package com.bmarket.tradeservice.domain.trade.repository.query.dto;

import com.bmarket.tradeservice.domain.trade.entity.Category;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor

@Getter
public class TradeDetailDto {
    private Long tradeId;
    private String nickName;
    private String title;
    private String context;
    private Category category;
    private String townName;
    private List<String> imagePath;
    @QueryProjection
    public TradeDetailDto(Long tradeId, String nickName, String title, String context, Category category, String townName) {
        this.tradeId = tradeId;
        this.nickName = nickName;
        this.title = title;
        this.context = context;
        this.category = category;
        this.townName = townName;
    }

    public void addImagePath(List<String> imagePath){
        this.imagePath = imagePath;
    }
}
