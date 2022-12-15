package com.bmarket.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

//@Configuration
public class FilterConfig {

//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("api")
                        .filters(f -> f
                                .addRequestHeader("", "")
                                .addResponseHeader("", ""))
                        .uri("http://loaclhost:8080"))
                .build();
    }
}
