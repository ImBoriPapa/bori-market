package com.bmarket.securityservice.cofig;

import com.bmarket.securityservice.filter.CustomAccessDeniedHandler;
import com.bmarket.securityservice.filter.CustomAuthenticationEntryPoint;
import com.bmarket.securityservice.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .authorizeRequests().antMatchers("/login", "/test","/exception","/exception/**","/api/address","/api/address/*","/image-test","/event","/event/*","/profile/**").permitAll()
                .antMatchers(HttpMethod.POST,"/account").permitAll()
                .antMatchers("/trade","/trade/**").hasAuthority("ROLL_USER")
                .antMatchers("/redirect1","/redirect2").permitAll()
                .antMatchers("/test-profile-image").permitAll()
                .antMatchers("/jwt-test1").authenticated()
                .antMatchers("/jwt-test1").hasAuthority("ROLL_USER")
                .antMatchers("/jwt-test2").hasAuthority("ROLL_ADMIN")
                .anyRequest().authenticated();
        return security.build();
    }

}
