package com.be.whereu.config.properties;

import com.be.whereu.model.property.TokenProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "token")
@Configuration
@Data
public class TokenPropertiesConfig {
    private TokenProperty registerToken;
    private TokenProperty accessToken;
    private TokenProperty refreshToken;
}
