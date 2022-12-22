package com.bmarket.tradeservice.api.responseForm;

import com.bmarket.tradeservice.domain.entity.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ResponseResult {
    private Long tradeId;
    private String memberId;
    private LocalDateTime createdAt;

    public ResponseResult(Trade trade) {
        this.tradeId = trade.getId();
        this.memberId = trade.getMemberId();
        this.createdAt = trade.getCreatedAt();
    }
}
