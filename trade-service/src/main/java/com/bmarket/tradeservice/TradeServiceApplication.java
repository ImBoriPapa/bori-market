package com.bmarket.tradeservice;

import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TradeServiceApplication {

	private final TradeRepository tradeRepository;

	public static void main(String[] args) {
		SpringApplication.run(TradeServiceApplication.class, args);


	}
	@PostConstruct
	void beforeEach() {
		List<Trade> trades = new ArrayList<>();
		/**
		 *  CASE 1:
		 *  CATEGORY= BOOK
		 *  Is Share= false
		 *  Is offer= false
		 *  AddressCode = 1001
		 *  20 건
		 */
		for (int i = 1; i <= 20; i++) {
			Trade trade = Trade.createTrade()
					.accountId(Long.valueOf(i))
					.profileImage("profile.jpg")
					.nickname("nickname" + i)
					.townName("동네" + i)
					.addressCode(1001)
					.price(100000)
					.title("제목" + i)
					.category(Category.BOOK)
					.context("내용이 있다")
					.isOffer(false)
					.isShare(false)
					.representativeImage("대표이미지.jpg").build();
			trades.add(trade);
		}
		/**
		 *  CASE 2:
		 *  CATEGORY= DIGITAL
		 *  Is Share= false
		 *  Is offer= false
		 *  AddressCode = 1001
		 *  20 건
		 */
		for (int i = 21; i <= 40; i++) {

			Trade trade = Trade.createTrade()
					.accountId(Long.valueOf(i))
					.profileImage("profile.jpg")
					.nickname("nickname" + i)
					.townName("동네" + i)
					.addressCode(1001)
					.price(100000)
					.title("제목" + i)
					.category(Category.DIGITAL)
					.context("내용이 있다")
					.isOffer(false)
					.isShare(false)
					.representativeImage("대표이미지.jpg").build();
			trades.add(trade);
		}
		/**
		 *  CASE 3:
		 *  CATEGORY= ETC
		 *  Is Share= true
		 *  Is offer= false
		 *  AddressCode = 1002
		 *  20 건
		 */
		for (int i = 41; i <= 60; i++) {
			Trade trade = Trade.createTrade()
					.accountId(Long.valueOf(i))
					.profileImage("profile.jpg")
					.nickname("nickname" + i)
					.townName("동네" + i)
					.addressCode(1002)
					.price(100000)
					.title("제목" + i)
					.category(Category.ETC)
					.context("내용이 있다")
					.isOffer(true)
					.isShare(true)
					.representativeImage("대표이미지.jpg").build();
			trades.add(trade);
		}
		/**
		 *  CASE 4:
		 *  CATEGORY= BEAUTY
		 *  Is Share= false
		 *  Is offer= false
		 *  AddressCode = 1010
		 *  20 건
		 */
		for (int i = 61; i <= 80; i++) {
			Trade trade = Trade.createTrade()
					.accountId(Long.valueOf(i))
					.profileImage("profile.jpg")
					.nickname("nickname" + i)
					.townName("동네" + i)
					.addressCode(1010)
					.price(100000)
					.title("제목" + i)
					.category(Category.BEAUTY)
					.context("내용이 있다")
					.isOffer(false)
					.isShare(false)
					.representativeImage("대표이미지.jpg").build();
			trades.add(trade);
		}

		/**
		 *  CASE 5:
		 *  CATEGORY= GAME
		 *  Is Share= false
		 *  Is offer= true
		 *  AddressCode = 1005
		 *  20 건
		 */
		for (int i = 81; i <= 100; i++) {
			Trade trade = Trade.createTrade()
					.accountId(Long.valueOf(i))
					.profileImage("profile.jpg")
					.nickname("nickname" + i)
					.townName("동네" + i)
					.addressCode(1005)
					.price(100000)
					.title("제목" + i)
					.category(Category.GAME)
					.context("내용이 있다")
					.isOffer(true)
					.isShare(false)
					.representativeImage("대표이미지.jpg").build();
			trades.add(trade);
		}
		tradeRepository.saveAll(trades);
	}

}
