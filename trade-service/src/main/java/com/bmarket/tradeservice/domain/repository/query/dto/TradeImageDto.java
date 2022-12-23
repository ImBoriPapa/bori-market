package com.bmarket.tradeservice.domain.repository.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TradeImageDto {

    private String originalFileName;
    private String fullPath;

    @QueryProjection
    public TradeImageDto(String originalFileName, String fullPath) {
        this.originalFileName = originalFileName;
        this.fullPath = fullPath;
    }
}
