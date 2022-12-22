package com.bmarket.tradeservice.domain.service;

import com.bmarket.tradeservice.api.requestForm.RequestForm;
import com.bmarket.tradeservice.api.requestForm.RequestUpdateForm;
import com.bmarket.tradeservice.domain.entity.*;
import com.bmarket.tradeservice.domain.repository.TradeImageRepository;
import com.bmarket.tradeservice.domain.repository.TradeRepository;
import com.bmarket.tradeservice.exception.custom_exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
@Transactional
class TradeCommandServiceTest {

    @Autowired
    TradeCommandService tradeCommandService;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeImageRepository tradeImageRepository;
    @Autowired
    EntityManager em;
    private String member =UUID.randomUUID().toString();
    @BeforeEach
    void before() {
        log.info("[TEST DATA INIT]");
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(member)
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.jpeg", "image/png", "imageContent".getBytes());

        List<MultipartFile> images = List.of(image1, image2, image3);
        tradeCommandService.createTrade(form, images);
        log.info("[TEST DATA INIT FINISH]");
    }

    @AfterEach
    void after() {
        log.info("[TEST DATA DELETE]");
        tradeRepository.deleteAll();
        tradeImageRepository.deleteAll();
    }

    @Test
    @DisplayName("createTrade 테스트")
    void createTradeTest() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(UUID.randomUUID().toString())
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image4 = new MockMultipartFile("images", "image4.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image5 = new MockMultipartFile("images", "image5.png", "image/png", "imageContent".getBytes());
        List<MultipartFile> images = List.of(image1, image2, image3, image4, image5);
        //when
        Trade trade = tradeCommandService.createTrade(form, images);
        Trade findTrade = tradeRepository.findById(trade.getId()).orElseThrow(IllegalAccessError::new);
        List<TradeImage> imageList = tradeImageRepository.findAllByTrade(trade);

        //then
        assertThat(findTrade.getId()).isEqualTo(trade.getId());
        assertThat(findTrade.getMemberId()).isEqualTo(trade.getMemberId());
        assertThat(findTrade.getTitle()).isEqualTo(trade.getTitle());
        assertThat(imageList.size()).isEqualTo(5);
        assertThat(imageList.stream()
                .map(TradeImage::getOriginalFileName)
                .collect(Collectors.toList())
                .containsAll(images.stream()
                        .map(MultipartFile::getOriginalFilename)
                        .collect(Collectors.toList())))
                .isTrue();
    }

    @Test
    @DisplayName("createTrade 롤백 테스트 : 지원하지 않는 확장자 포함")
    void createTradeTest2() throws Exception {
        //given
        String memberId = java.util.UUID.randomUUID().toString();
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(memberId)
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpeg", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.wrongExt", "image/png", "imageContent".getBytes());

        List<MultipartFile> images = List.of(image1, image2, image3);
        //when

        assertThatThrownBy(() -> tradeCommandService.createTrade(form, images))
                .isInstanceOf(FileUploadException.class);

        //then

    }

    @Test
    @DisplayName("createTrade 롤백 테스트 : 잘못된 파일 형식")
    void createTradeTest3() throws Exception {
        //given
        String memberId = java.util.UUID.randomUUID().toString();
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(memberId)
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "wrongFileName", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.jpeg", "image/png", "imageContent".getBytes());

        List<MultipartFile> images = List.of(image1, image2, image3);
        //when
        assertThatThrownBy(() -> tradeCommandService.createTrade(form, images))
                .isInstanceOf(FileUploadException.class);
        //then

    }

    @Test
    @DisplayName("createTrade 롤백 테스트 : memberId 누락")
    void createTradeTest4() throws Exception {
        //given
        Address address = Address.builder()
                .addressCode(1001)
                .city("서울")
                .district("서울구")
                .town("서울동")
                .build();
        RequestForm form = RequestForm.builder()
                .memberId(null)
                .title("제목1")
                .context("내용1")
                .price(10000)
                .address(address)
                .category(Category.PAT)
                .isOffer(false)
                .tradeType(TradeType.USED_GOODS).build();

        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "image3.jpeg", "image/png", "imageContent".getBytes());

        List<MultipartFile> images = List.of(image1, image2, image3);
        //when
        assertThatThrownBy(() -> tradeCommandService.createTrade(form, images))
                .isInstanceOf(RuntimeException.class);
        //then
        em.clear();

    }

    @Test
    @DisplayName("deleteTrade 테스트")
    void deleteTradeTest1() throws Exception {
        //given
        String memberId = this.member;
        Trade trade = tradeRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("trade를 찾을 수 없습니다."));
        //when
        tradeCommandService.deleteTrade(trade.getId());

        //then
        assertThatThrownBy(() -> tradeRepository.findById(trade.getId())
                        .orElseThrow(() -> new IllegalArgumentException("trade를 찾을 수 없습니다.")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("updateTrade 테스트")
    void updateTradeTest1() throws Exception{
        //given
        Trade trade = tradeRepository.findByMemberId(member)
                .orElseThrow(()-> new IllegalArgumentException("trade를 찾을 수 없습니다."));

        RequestUpdateForm updateForm = RequestUpdateForm.builder()
                .title("새로운")
                .context("내용")
                .price(30000)
                .category(Category.BEAUTY)
                .tradeType(TradeType.ADVERTISEMENT)
                .tradeStatus(TradeStatus.TRADING)
                .isOffer(true)
                .build();
        MockMultipartFile image1 = new MockMultipartFile("images", "newImage1.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "newImage2.png", "image/png", "imageContent".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("images", "newImage3.jpeg", "image/png", "imageContent".getBytes());

        List<MultipartFile> images = List.of(image1, image2, image3);
        //when
        Trade updated = tradeCommandService.updateTrade(trade.getId(), updateForm, images);
        List<TradeImage> imageList = tradeImageRepository.findAllByTrade(updated);

        //then
        assertThat(updated.getId()).isEqualTo(trade.getId());
        assertThat(updated.getTitle()).isEqualTo("새로운");
        assertThat(updated.getContext()).isEqualTo("내용");
        assertThat(updated.getPrice()).isEqualTo(30000);
        assertThat(updated.getCategory()).isEqualTo(Category.BEAUTY);
        assertThat(updated.getTradeType()).isEqualTo(TradeType.ADVERTISEMENT);
        assertThat(updated.getTradeStatus()).isEqualTo(TradeStatus.TRADING);
        assertThat(updated.getIsOffer()).isTrue();
        assertThat(imageList.get(0).getOriginalFileName()).isEqualTo(image1.getOriginalFilename());
    }

    @Test
    @DisplayName("updateTrade 테스트 : 이미지 파일없을 경우")
    void updateTradeTest2() throws Exception{
        //given
        Trade trade = tradeRepository.findByMemberId(member)
                .orElseThrow(()-> new IllegalArgumentException("trade를 찾을 수 없습니다."));

        RequestUpdateForm updateForm = RequestUpdateForm.builder()
                .title("새로운")
                .context("내용")
                .price(30000)
                .category(Category.BEAUTY)
                .tradeType(TradeType.ADVERTISEMENT)
                .tradeStatus(TradeStatus.TRADING)
                .isOffer(true)
                .build();

        //when
        Trade updated = tradeCommandService.updateTrade(trade.getId(), updateForm, null);
        List<TradeImage> imageList = tradeImageRepository.findAllByTrade(updated);

        //then
        assertThat(updated.getId()).isEqualTo(trade.getId());
        assertThat(updated.getTitle()).isEqualTo("새로운");
        assertThat(updated.getContext()).isEqualTo("내용");
        assertThat(updated.getPrice()).isEqualTo(30000);
        assertThat(updated.getCategory()).isEqualTo(Category.BEAUTY);
        assertThat(updated.getTradeType()).isEqualTo(TradeType.ADVERTISEMENT);
        assertThat(updated.getTradeStatus()).isEqualTo(TradeStatus.TRADING);
        assertThat(updated.getIsOffer()).isTrue();
        assertThat(imageList.get(0).getOriginalFileName()).isEqualTo("image1.png");
    }

}