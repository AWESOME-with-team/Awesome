package com.be.whereu.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRequest {
    @JsonProperty("access_jws")
    private String accessJws;

    @JsonProperty("refresh_jws")
    private String refreshJws;

}