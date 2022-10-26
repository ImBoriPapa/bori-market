package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.TradeDto;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;


    public Trade createTrade(TradeDto dto) {



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
                .build();
        Trade save = tradeRepository.save(trade);

        Image imagePath = getImagePath(save.getId(), dto.getImages());

        ArrayList<TradeImage> images = new ArrayList<>();

        imagePath.getImagePath().stream().forEach(m -> {
            TradeImage tradeImage = TradeImage.createImage()
                    .imageName(m)
                    .trade(trade).build();
            images.add(tradeImage);});
        List<TradeImage> imageList = tradeImageRepository.saveAll(images);
        save.updateRepresentativeImage(imageList.get(0).getImageName());
        return save;
    }

    private Image getImagePath(Long tradeId, List<MultipartFile> files) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("tradeId", tradeId);
        files.stream().forEach(m -> builder.part("images", m.getResource()));

        Image image = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/trade")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Image.class)
                .block();
        return image;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Image {
        private List<String> imagePath;
    }
}
