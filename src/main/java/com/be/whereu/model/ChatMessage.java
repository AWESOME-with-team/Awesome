package com.be.whereu.model;


import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private String chatId;
}
