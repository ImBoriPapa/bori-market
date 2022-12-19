package com.bmarket.securityservice.config;


import com.bmarket.securityservice.filter.JwtAuthenticationFilter;
import com.bmarket.securityservice.security.service.UserDetailServiceImpl;
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
    private final UserDetailServiceImpl userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

        security.httpBasic().disable();
        security.cors().disable();
        security.csrf().disable();
        security.formLogin().disable();
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .userDetailsService(userDetailService)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/account", "/login").permitAll()
                .antMatchers("/refresh", "/refresh/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated();
        return security.build();
    }
}
