package com.be.whereu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatDto {
    private Integer channelId;
    private Integer writerId;
    private String chat;
}