package com.bmarket.frmservice.domain.trade.service;

import com.bmarket.frmservice.domain.trade.dto.ResponseTradeImage;
import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bmarket.frmservice.utils.Patterns.SEARCH_TRADE_PATTERN;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TradeImageService {

    @Value("${resource-path.trade-image-path}")
    private String IMAGE_PATH;
    private final TradeImageRepository tradeImageRepository;
    private final FileManager fileManager;

    /**
     * tradeImage 생성
     * 만약 tradeId 가 중복된다면 업데이트
     * tradeId, 이미지 파일 저장 후 저장된 이미지 파일 경로 반환
     */
    public ResponseTradeImage createTradeImage(Long tradeId, List<MultipartFile> files) {

        if (tradeImageRepository.findByTradeId(tradeId).isPresent()) {
            return updateTradeImage(tradeId, files);
        }

        List<UploadFile> uploadFiles = fileManager.saveFile(IMAGE_PATH, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .tradeId(tradeId)
                .images(uploadFiles).build();

        TradeImage save = tradeImageRepository.save(tradeImage);

        List<String> imagePathList = getImagePathList(save);

        return ResponseTradeImage
                .builder()
                .success(true)
                .tradeId(save.getTradeId())
                .imagePath(imagePathList)
                .build();
    }

    /**
     * 이미지 접근 경로 반환
     * SEARCH_TRADE_PATTERN+storedImageName 이미지 접근 경로
     */
    private List<String> getImagePathList(TradeImage save) {
        List<String> list = save.getImages()
                .stream()
                .map(UploadFile::getStoredImageName)
                .map(m -> SEARCH_TRADE_PATTERN + m)
                .collect(Collectors.toList());
        return list;
    }

    /**
     * TradeImage 수정
     */
    public ResponseTradeImage updateTradeImage(Long tradeId, List<MultipartFile> files) {
        TradeImage tradeImage = findProfileImageByTradeId(tradeId);
        //저장된 파일 삭제
        deleteStoredImage(tradeImage.getImages());
        //새 파일 저장
        List<UploadFile> uploadFiles = fileManager.saveFile(IMAGE_PATH, files);
        //엔티티 업데이트
        tradeImage.updateTradeImage(uploadFiles);

        TradeImage updated = tradeImageRepository.save(tradeImage);

        //이미지 경로 생성
        List<String> imagePathList = getImagePathList(tradeImage);

        return ResponseTradeImage.builder()
                .success(true)
                .tradeId(updated.getTradeId())
                .imagePath(imagePathList)
                .build();
    }

    /**
     * 판매 상품 이미지 삭제
     */
    public ResponseTradeImage deleteImages(Long tradeId) {
        TradeImage tradeImage = findProfileImageByTradeId(tradeId);

        List<UploadFile> images = tradeImage.getImages();

        deleteStoredImage(images);

        tradeImageRepository.delete(tradeImage);

        return ResponseTradeImage.builder()
                .success(true)
                .tradeId(tradeId)
                .imagePath(Collections.emptyList())
                .build();
    }

    private void deleteStoredImage(List<UploadFile> images) {
        images.forEach(image -> fileManager.deleteFile(IMAGE_PATH, image.getStoredImageName()));
    }

    private TradeImage findProfileImageByTradeId(Long tradeId) {
        TradeImage tradeImage = tradeImageRepository.findByTradeId(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("trade image 를 찾을 수 없습니다."));
        return tradeImage;
    }
}
