package com.bmarket.frmservice.domain.trade.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "TRADE_IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TradeImage {

    @Id
    private String id;
    private List<UploadFile> images = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder(builderMethodName = "createTradeImage")
    public TradeImage(List<UploadFile> images) {
        this.images = images;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTradeImage(List<UploadFile> images) {
        this.images.clear();
        this.images = images;
        this.updatedAt = LocalDateTime.now();
    }
}

