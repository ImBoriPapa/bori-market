package com.bmarket.tradeservice.domain.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TradeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORIGINAL_NAME")
    private String originalFileName;

    @Column(name = "STORED_NAME")
    private String storedFileName;

    @Column(name = "FULL_PATH")
    private String fullPath;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "TYPE")
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_ID")
    private Trade trade;

    /**
     * 판매글 이미지 정보 생성 메서드
     */
    @Builder(builderMethodName = "createImage")
    public TradeImage(String originalFileName, String storedFileName, String fullPath, Long size, String fileType, Trade trade) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fullPath = fullPath;
        this.size = size;
        this.fileType = fileType;
        this.trade = trade;
    }
}
