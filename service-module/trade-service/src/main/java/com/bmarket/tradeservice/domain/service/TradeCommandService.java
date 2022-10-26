package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.internal.RequestImageApi;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TradeCommandService {
    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;
    private final RequestImageApi requestImageApi;

    public Trade createTrade(RequestForm form, List<MultipartFile> files) {
        Trade trade = Trade.createTrade()
                .accountId(form.getAccountId())
                .profileImage(form.getProfileImage())
                .nickname(form.getNickname())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .addressCode(form.getAddressCode())
                .townName(form.getTownName())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer())
                .build();
        Trade newTrade = tradeRepository.save(trade);
        ArrayList<TradeImage> images = new ArrayList<>();

        requestImageApi.getImagePath(newTrade.getId(), files)
                .getImagePath().forEach(m -> {
                    TradeImage tradeImage = TradeImage.createImage()
                            .imageName(m)
                            .trade(trade).build();
                    images.add(tradeImage);
                });
        List<TradeImage> imageList = tradeImageRepository.saveAll(images);
        newTrade.updateRepresentativeImage(imageList.get(0).getImageName());
        return newTrade;
    }
}
