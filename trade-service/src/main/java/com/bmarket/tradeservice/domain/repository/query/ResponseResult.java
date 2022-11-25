package com.bmarket.tradeservice.domain.repository.query;

import com.bmarket.tradeservice.domain.repository.query.dto.TradeListDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseResult<T> {

    private Integer size;
    private Boolean hasNext;
    private List<TradeListDto> result = new ArrayList<>();

    public ResponseResult(int size, Boolean hasNext, List<TradeListDto> result) {
        this.size = size;
        this.hasNext = hasNext;
        this.result = result;
    }
}
