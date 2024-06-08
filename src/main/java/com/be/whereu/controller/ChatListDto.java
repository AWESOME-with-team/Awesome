package com.be.whereu.controller;

import com.be.whereu.model.Rtype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatListDto {
    private Long chatId;
    private String chatName;
    private String content;
    private Rtype chatType;
    private int count;
    private String lastChatAt;
}