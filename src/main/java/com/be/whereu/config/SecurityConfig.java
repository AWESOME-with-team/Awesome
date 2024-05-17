package com.be.whereu.config;

import com.be.whereu.filter.JwtFilter;
import com.be.whereu.security.handler.CustomAuthenticationFailureHandler;
import com.be.whereu.security.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;




@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationSuccessHandler SuccessHandler;
    private final CustomAuthenticationFailureHandler FailureHandler;
    private final String[] whiteList = {
            "/h2-console/**",
            "/favicon.ico",
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/**",
            "/room/**"
            };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); // flutter로 경로설정을 해야함
        http.formLogin(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers("/input").hasAnyRole("GUEST","USER")
                        //.requestMatchers("/**").hasRole("USER")
                        .anyRequest().permitAll()

        );


        http.oauth2Login(o->{
           o.successHandler(SuccessHandler);
           o.failureHandler(FailureHandler);
           o.permitAll();
        });

        //session 끄기
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

