package com.be.whereu.model.dto;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDto {
    private Integer channelId;
    private Integer writerId;
    private String chat;
}