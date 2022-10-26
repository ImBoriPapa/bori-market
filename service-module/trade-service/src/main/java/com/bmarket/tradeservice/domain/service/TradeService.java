package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.TradeDto;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;


    public Trade createTrade(TradeDto dto) {

        MultipartFile file = dto.getImages().stream().collect(Collectors.toList()).get(0);

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
                .representativeImage(file.getOriginalFilename()).build();
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

    private String getImagePath(Long tradeId, List<MultipartFile> files) {

        List<Resource> resources = new ArrayList<>();

        for (MultipartFile file : files) {
            resources.add(file.getResource());
        }

        String profileImage = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("tradeId", tradeId)
                        .with("images", resources))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return profileImage;
    }
}
