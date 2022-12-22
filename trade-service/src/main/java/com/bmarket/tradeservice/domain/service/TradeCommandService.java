package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.api.requestForm.RequestForm;
import com.bmarket.tradeservice.api.requestForm.RequestUpdateForm;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.dto.ImageDetailDto;
import com.bmarket.tradeservice.utils.imageSupport.ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("[createTrade -> request memberId= {},images size ={}]", form.getMemberId(), images.size());

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
        log.info("[createTrade ->tradeRepository.save ]");
        Trade save = tradeRepository.save(trade);
        log.info("[createTrade ->imageUploader.uploadFile ]");
        List<ImageDetailDto> imageDetailDtoList = imageUploader.uploadFile(images, "trade");
        log.info("[createTrade ->dtoListToTradeImageList.imageList ]");
        List<TradeImage> imageList = dtoListToTradeImageList(trade, imageDetailDtoList);
        log.info("[createTrade ->tradeImageRepository.saveAll ]");
        List<TradeImage> tradeImages = tradeImageRepository.saveAll(imageList);
        log.info("[createTrade ->updateRepresentativeImage ]");
        save.updateRepresentativeImage(tradeImages.get(0).getFullPath());

        return save;
    }

    private List<TradeImage> dtoListToTradeImageList(Trade trade, List<ImageDetailDto> imageDetailDtoList) {
        log.info("[createTrade -> dtoListToTradeImageList()]");
        return imageDetailDtoList.stream()
                .map(data ->
                        TradeImage.createImage()
                                .originalFileName(data.getOriginalFileName())
                                .storedFileName(data.getStoredFileName())
                                .fullPath(data.getFullPath())
                                .size(data.getSize())
                                .fileType(data.getFileType())
                                .trade(trade).build()).collect(Collectors.toList());
    }


    /**
     * 판매글 삭제
     */
    public void deleteTrade(Long tradeId) {
        log.info("deleteTrade]");
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("trade를 찾응 수 없습니다."));

        List<TradeImage> images = findImages(trade);

        imageUploader.deleteFile(images.stream().map(TradeImage::getStoredFileName).collect(Collectors.toList()));

        tradeImageRepository.deleteAll(images);

        tradeRepository.delete(trade);

    }

    /**
     * 판매글 수정
     */
    public Trade updateTrade(Long tradeId, RequestUpdateForm form, List<MultipartFile> files) {
        log.info("[updateTrade]");
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("trade를 찾을 수 없습니다."));

        updateImages(files, trade);
        /**
         * 업데이트 필드를 빌더패턴으로 구현
         */
        log.info("[updateTrade -> update trade]");
        Trade.UpdateForm updateForm = Trade.UpdateForm.builder()
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .isOffer(form.getIsOffer())
                .address(form.getAddress())
                .tradeStatus(form.getTradeStatus())
                .tradeType(form.getTradeType())
                .build();
        trade.updateTrade(updateForm);

        String representativeImage = updateImages(files, trade);
        log.info("[updateTrade -> updateRepresentativeImage]");
        trade.updateRepresentativeImage(representativeImage);
        return trade;
    }

    /**
     * 판매글 상태 변경s
     */
    public void updateStatus(Long tradeId, TradeStatus status) {
        tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("trade를 찾을 수 없습니다."))
                .updateStatus(status);
    }



    /**
     * 이미지 수정
     */
    private String updateImages(List<MultipartFile> files, Trade trade) {
        log.info("[updateImages]");
        List<TradeImage> findImages = findImages(trade);
        log.info("[updateImages -> deleteFile]");
        imageUploader.deleteFile(findImages.stream().map(TradeImage::getStoredFileName).collect(Collectors.toList()));
        log.info("[updateImages -> uploadFile]");
        List<ImageDetailDto> uploadFile = imageUploader.uploadFile(files, "trade");
        log.info("[updateImages -> delete trade images]");
        tradeImageRepository.deleteAllById(findImages.stream().map(TradeImage::getId).collect(Collectors.toList()));
        log.info("[updateImages -> new trade images]");
        List<TradeImage> tradeImages = dtoListToTradeImageList(trade, uploadFile);
        log.info("[updateImages -> save trade images]");
        List<TradeImage> saveAll = tradeImageRepository.saveAll(tradeImages);
        return saveAll.get(0).getFullPath();
    }

    private List<TradeImage> findImages(Trade trade) {
        return tradeImageRepository.findAllByTrade(trade);
    }


}
