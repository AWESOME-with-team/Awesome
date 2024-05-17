package com.be.whereu.model.property;

import lombok.Data;

@Data
public class TokenProperty {
    String secret;
    Integer expiration;
}
