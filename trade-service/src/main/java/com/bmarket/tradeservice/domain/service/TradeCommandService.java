package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.dto.RequestForm;
import com.bmarket.tradeservice.dto.RequestUpdateForm;
import com.bmarket.tradeservice.dto.ResponseImageDto;
import com.bmarket.tradeservice.utils.ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bmarket.tradeservice.exception.ExceptionMessage.NOTFOUND_TRADE;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TradeCommandService {
    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;
    private final ImageUploader imageUploader;

    /**
     * 판매글 생성
     */
    public Trade createTrade(RequestForm form, List<MultipartFile> images) {

        Trade trade = Trade.createTrade()
                .memberId(form.getMemberId())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .address(form.getAddress())
                .category(form.getCategory())
                .isOffer(form.getIsOffer())
                .tradeType(form.getTradeType())
                .representativeImage(null)
                .build();
        Trade save = tradeRepository.save(trade);

        ArrayList<TradeImage> imageList = new ArrayList<>();

        imageUploader.uploadFile(images, "trade").stream()
                .map(data ->
                        TradeImage.createImage()
                                .originalFileName(data.getOriginalFileName())
                                .storedFileName(data.getStoredFileName())
                                .fullPath(data.getFullPath())
                                .size(data.getSize())
                                .fileType(data.getFileType())
                                .trade(trade).build())
                .forEach(result -> imageList.add(result));

        List<TradeImage> tradeImages = tradeImageRepository.saveAll(imageList);

        save.updateRepresentativeImage(tradeImages.get(0).getFullPath());

        return save;
    }

    /**
     * 응답 받은 이미지 경로로 TradeImage 객체 생성
     */
    private void saveImageList(ResponseImageDto responseImageDto, Trade trade) {
        ArrayList<TradeImage> images = new ArrayList<>();

        log.info("responseImageDto.getTradeImageId() ={}", responseImageDto.getImageId());


        tradeImageRepository.saveAll(images);
    }

    /**
     * 판매글 삭제
     */
    public void deleteTrade(Long tradeId) {
        Trade trade = findTrade(tradeId);

        List<TradeImage> byTrade = findImages(trade);


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
        Trade.UpdateForm updateForm = Trade.UpdateForm.builder()
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .context(form.getContext())
                .address(form.getAddress())
                .category(form.getCategory())
                .isOffer(form.getIsOffer()).build();

        trade.updateTrade(updateForm);

        return trade;
    }

    /**
     * 이미지 수정
     */
    private void updateImages(List<MultipartFile> files, Trade trade) {
        if (files.size() != 0) {
            List<TradeImage> findImages = findImages(trade);


            tradeImageRepository.deleteAllById(findImages.stream().map(TradeImage::getId).collect(Collectors.toList()));


        }
    }

    private List<TradeImage> findImages(Trade trade) {
        return tradeImageRepository.findAllByTrade(trade);
    }

    private Trade findTrade(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUND_TRADE.getMessage()));
    }
}
