package com.be.whereu.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "profile")
@Configuration
@Data
public class ProfileImageConfig {
    private String profileImagePath;
}
