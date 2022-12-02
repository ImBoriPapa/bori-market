package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.dto.RequestUpdateForm;
import com.bmarket.tradeservice.domain.dto.ResponseImageDto;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.internal.RequestImageApi;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bmarket.tradeservice.domain.exception.ExceptionMessage.NOTFOUND_TRADE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TradeCommandService {
    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;
    private final RequestImageApi requestImageApi;

    /**
     * 판매글 생성
     */
    public Trade createTrade(RequestForm form, List<MultipartFile> files) {

        ResponseImageDto responseImageDto = requestImageApi.postTradeImages(files);

        Trade trade = Trade.createTrade()
                .accountId(form.getAccountId())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .address(form.getAddress())
                .representativeImage(responseImageDto.getImagePath().get(0))
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer())
                .build();
        Trade newTrade = tradeRepository.save(trade);

        saveImageList(responseImageDto, trade);

        return newTrade;
    }

    /**
     * 응답 받은 이미지 경로로 TradeImage 객체 생성
     */
    private void saveImageList(ResponseImageDto responseImageDto, Trade trade) {
        ArrayList<TradeImage> images = new ArrayList<>();

        log.info("responseImageDto.getTradeImageId() ={}", responseImageDto.getImageId());

        responseImageDto
                .getImagePath()
                .forEach(m -> {
                    TradeImage tradeImage = TradeImage.createImage()
                            .imageId(responseImageDto.getImageId())
                            .trade(trade)
                            .imagePath(m)
                            .build();
                    images.add(tradeImage);
                });

        tradeImageRepository.saveAll(images);
    }

    /**
     * 판매글 삭제
     */
    public void deleteTrade(Long tradeId) {
        Trade trade = findTrade(tradeId);

        List<TradeImage> byTrade = findImages(trade);

        requestImageApi.deleteTradeImages(byTrade.get(0).getImageId());

        tradeImageRepository.deleteAll(byTrade);

        tradeRepository.delete(trade);
    }

    /**
     * 판매글 수정
     */
    public Trade updateTrade(Long tradeId, RequestUpdateForm form, List<MultipartFile> files) {

        Trade trade = findTrade(tradeId);

        updateImages(files, trade);
        /**
         * 업데이트 필드를 빌더패턴으로 구현
         */
        Trade.UpdateBuilder updateBuilder = Trade.UpdateBuilder.builder()
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .context(form.getContext())
                .address(form.getAddress())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer()).build();

        trade.updateTrade(updateBuilder);

        return trade;
    }

    /**
     * 이미지 수정
     */
    private void updateImages(List<MultipartFile> files, Trade trade) {
        if (files.size() != 0) {
            List<TradeImage> findImages = findImages(trade);
            ResponseImageDto responseImageDto = requestImageApi.updateTradeImages(findImages.get(0).getImageId(), files);

            tradeImageRepository.deleteAllById(findImages.stream().map(TradeImage::getId).collect(Collectors.toList()));

            saveImageList(responseImageDto, trade);
            trade.updateRepresentativeImage(responseImageDto.getImagePath().get(0));
        }
    }

    private List<TradeImage> findImages(Trade trade) {
        return tradeImageRepository.findByTrade(trade);
    }

    private Trade findTrade(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUND_TRADE.getMessage()));
    }
}
