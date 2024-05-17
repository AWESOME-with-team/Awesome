package com.be.whereu.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "univcert")
@Configuration
@Data
public class UnivPropertiesConfig {


  private String univKey;


}
