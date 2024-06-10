package com.be.whereu.service;


import com.be.whereu.model.dto.ChatMemberDto;
import com.be.whereu.model.entity.ChatMemberEntity;

public interface ChatMemberService {
    public boolean insert(ChatMemberDto dto);
}
