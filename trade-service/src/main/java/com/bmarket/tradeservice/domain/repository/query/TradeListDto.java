package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.domain.repository.query.dto.TradeListResult;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TradeListDto {
    private Integer size;
    private Boolean hasNext;
    private List<TradeListResult> result = new ArrayList<>();

    public TradeListDto(int size, Boolean hasNext, List<TradeListResult> result) {
        this.size = size;
        this.hasNext = hasNext;
        this.result = result;
    }
}
