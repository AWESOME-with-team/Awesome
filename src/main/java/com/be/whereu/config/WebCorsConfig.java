package com.be.whereu.config;

import com.be.whereu.config.properties.CorsPropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Slf4j
@Component
@RequiredArgsConstructor
public class WebCorsConfig implements WebMvcConfigurer {
    private final CorsPropertiesConfig corsConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsConfig.getAllowedOrigins())
              //  .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
        log.info("cors 경로: {}"+corsConfig.getAllowedOrigins().toString());

    }
}
