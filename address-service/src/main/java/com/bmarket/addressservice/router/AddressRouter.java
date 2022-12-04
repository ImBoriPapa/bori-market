package com.bmarket.addressservice.router;

import com.bmarket.addressservice.handler.AddressHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class AddressRouter {
    /**
     * URL 맵핑
     */
    @Bean
    public RouterFunction<ServerResponse> search(AddressHandler addressHandler) {
        return route()
                .GET("/internal/address", addressHandler::searchByTown)
                .GET("/address/search-form",addressHandler::searchForm)
                .build();
    }
}
