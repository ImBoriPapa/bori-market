package com.bmarket.apigatewayservice.Filter;


import com.bmarket.apigatewayservice.exception.cutom_exception.ResponseStatus;
import com.bmarket.apigatewayservice.exception.cutom_exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


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

            String accessToken = resolveToken(exchange.getRequest(), HttpHeaders.AUTHORIZATION);

            if (!StringUtils.hasLength(accessToken)) {
                log.info("[CAN NOT FOUND ACCESS TOKEN]");
                throw new TokenException(ResponseStatus.EMPTY_ACCESS_TOKEN);
            }

            if (validateToken(accessToken) == JwtCode.ACCESS) {
                log.info("[ACCESS TOKEN IS OK]");
            }

            if (validateToken(accessToken) == JwtCode.DENIED) {
                log.info("[ACCESS TOKEN IS DENIED]");
                throw new TokenException(ResponseStatus.DENIED_ACCESS_TOKEN);
            }

            if (validateToken(accessToken) == JwtCode.EXPIRED) {
                log.info("[ACCESS TOKEN IS EXPIRED]");
                throw new TokenException(ResponseStatus.EXPIRED_ACCESS_TOKEN);
            }

            return chain.filter(exchange);
        }));
    }


    private static String resolveToken(ServerHttpRequest request, String header) {

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new TokenException(ResponseStatus.NOT_FOUND_ACCESS_TOKEN);
        }

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

    public static class Config {

    }


}
