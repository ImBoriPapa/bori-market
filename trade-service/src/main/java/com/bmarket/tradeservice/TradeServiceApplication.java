package com.bmarket.tradeservice;


import com.bmarket.tradeservice.domain.sample.SampleProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@SpringBootApplication
@RequiredArgsConstructor
public class TradeServiceApplication {

	private final SampleProvider provider;

	public static void main(String[] args) {
		SpringApplication.run(TradeServiceApplication.class, args);

	}

	@PostConstruct
	@Profile("test")
	public void initSample(){
		provider.initSampleData();
	}
	@PreDestroy
	@Profile("test")
	public void clearSample(){
		provider.deleteSampleData();
	}

}
