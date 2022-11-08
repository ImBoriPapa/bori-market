package com.bmarket.frmservice.domain.trade.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "TRADE_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TradeImage {

    @Id
    private String id;
    private Long tradeId;
    private List<UploadFile> images = new ArrayList<>();

    @Builder(builderMethodName = "createTradeImage")
    public TradeImage(Long tradeId, List<UploadFile> images) {
        this.tradeId = tradeId;
        this.images = images;
    }
}

