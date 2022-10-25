package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.TradeDto;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;

    public Trade createTrade(TradeDto dto) {

        List<MultipartFile> fileList = dto.getImages().stream().collect(Collectors.toList());
        MultipartFile file = fileList.get(0);
        String originalFilename = file.getOriginalFilename();

        Trade trade = Trade.createTrade()
                .accountId(dto.getAccountId())
                .profileImage(dto.getProfileImage())
                .nickname(dto.getNickname())
                .title(dto.getTitle())
                .context(dto.getContext())
                .price(dto.getPrice())
                .addressCode(dto.getAddressCode())
                .townName(dto.getTownName())
                .category(dto.getCategory())
                .isShare(dto.getIsShare())
                .isOffer(dto.getIsOffer())
                .representativeImage(originalFilename).build();
        Trade save = tradeRepository.save(trade);

        ArrayList<TradeImage> images = new ArrayList<>();

        dto.getImages().stream().forEach(m -> {
            TradeImage tradeImage = TradeImage.createImage()
                    .imageName(m.getOriginalFilename())
                    .trade(trade).build();
            images.add(tradeImage);
        });
        tradeImageRepository.saveAll(images);

        return save;
    }
}
