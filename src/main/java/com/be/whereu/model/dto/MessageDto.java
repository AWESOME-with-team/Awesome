package com.be.whereu.model.dto;

import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.MemberEntity;
import jakarta.persistence.*;

public class MessageDto {
    private Long id;
    private String content;
    private MemberDto member;
    private ChatDto chat;
}
