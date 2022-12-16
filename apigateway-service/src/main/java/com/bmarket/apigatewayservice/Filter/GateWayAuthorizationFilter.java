package com.bmarket.apigatewayservice.Filter;


import com.bmarket.apigatewayservice.ResponseRefresh;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class GateWayAuthorizationFilter extends AbstractGatewayFilterFactory<GateWayAuthorizationFilter.Config> {


    public String secret;

    public GateWayAuthorizationFilter(@Value("${custom-key.secret-key}") String secret) {
        super(Config.class);
        this.secret = secret;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(Headers.AUTHORIZATION_HEADER)) {
                log.info("[CAN NOT FOUND ACCESS TOKEN]");
                throw new IllegalArgumentException("AccessToken 을 확인할 수 없습니다.");
            }

            String accessToken = resolveToken(request, HttpHeaders.AUTHORIZATION);

            reIssueRefresh(accessToken)
                    .subscribe(data -> log.info("Access token ={}, Refresh Token= {}", data.getAccessToken(), data.getRefreshToken()));

            reIssueRefresh(accessToken)
                    .subscribe(data->Mono.error(new IllegalArgumentException(data.getAccessToken())));

            if (!StringUtils.hasLength(accessToken)) {
                log.info("[EMPTY ACCESS TOKEN]");
                throw new IllegalArgumentException("AccessToken 이 필요한 접근입니다.");
            }

            if (validateToken(accessToken) == JwtCode.ACCESS) {
                log.info("[ACCESS TOKEN IS OK]");
            }

            if (validateToken(accessToken) == JwtCode.DENIED) {
                log.info("[ACCESS TOKEN IS DENIED]");
                throw new IllegalArgumentException("잘못된 토큰입니다.");
            }

            if (validateToken(accessToken) == JwtCode.EXPIRED) {
                log.info("[ACCESS TOKEN IS EXPIRED]");

                if (!request.getHeaders().containsKey(Headers.REFRESH_HEADER)) {
                    log.info("[REFRESH TOKEN IS EXPIRED]");
                    throw new IllegalArgumentException("[REFRESH TOKEN IS EMPTY]");
                }

                String refreshToken = resolveToken(request, Headers.REFRESH_HEADER);

                if (validateToken(refreshToken) == JwtCode.ACCESS) {
                    log.info("REFRESH TOKEN IS OK");
                }

                if (validateToken(refreshToken) == JwtCode.DENIED) {
                    log.info("REFRESH TOKEN IS DENIED");
                    throw new IllegalArgumentException("[REFRESH TOKEN IS DENIED]");
                }

                if (validateToken(refreshToken) == JwtCode.EXPIRED) {
                    log.info("REFRESH TOKEN IS EXPIRED");

                }


            }

            return chain.filter(exchange);
        }));
    }

    public Mono<ResponseRefresh> reIssueRefresh(String token) {
        String uri = "http://localhost:8000/security-service/refresh";
        log.info("[IS CALL?]");
        return WebClient.builder().baseUrl(uri)
                .build()
                .get()
                .uri("/{token}", token)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, status -> Mono.error(new IllegalArgumentException("리프레시 토큰에 문제가 있습니다.")))
                .bodyToMono(ResponseRefresh.class);
    }

    private static String resolveToken(ServerHttpRequest request, String header) {
        String authorization = request.getHeaders().get(header).get(0);
        return authorization.replace("Bearer-", "");
    }

    // 토큰의 유효성 + 만료일자 확인
    public JwtCode validateToken(String token) {
        log.info("==============[JWT_UTILS] 토큰 검증  =============");
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return JwtCode.DENIED;
        }
    }

    public enum JwtCode {
        ACCESS, EXPIRED, DENIED;
    }

    public static class Headers {
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String REFRESH_HEADER = "Refresh";
    }


    public static class Config {

    }


}
