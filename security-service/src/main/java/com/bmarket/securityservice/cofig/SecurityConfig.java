package com.bmarket.securityservice.cofig;

import com.bmarket.securityservice.filter.CustomAccessDeniedHandler;
import com.bmarket.securityservice.filter.CustomAuthenticationEntryPoint;
import com.bmarket.securityservice.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.httpBasic().disable();
        security.cors().disable();
        security.csrf().disable();
        security.formLogin().disable();
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        security.exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint);
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/docs/index.html").permitAll()
                .antMatchers("/login", "/test","/exception","/exception/**","/api/address","/api/address/*","/image-test","/event","/event/*","/profile/**").permitAll()
                .antMatchers(HttpMethod.POST,"/account").permitAll()
                .antMatchers("/trade","/trade/**").hasAuthority("ROLL_USER")
                .antMatchers("/redirect1","/redirect2").permitAll()
                .antMatchers("/test-profile-image").permitAll()
                .antMatchers("/jwt-test1").authenticated()
                .antMatchers("/jwt-test1").hasAuthority("ROLL_USER")
                .antMatchers("/jwt-test2").hasAuthority("ROLL_ADMIN")
                .antMatchers("/hello").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated();
        return security.build();
    }

    /**
     * 정적 리소스 허용
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
}
