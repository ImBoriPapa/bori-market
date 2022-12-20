package com.bmarket.tradeservice;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@RequiredArgsConstructor
@EnableDiscoveryClient
public class TradeServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(TradeServiceApplication.class, args);

    }

}
