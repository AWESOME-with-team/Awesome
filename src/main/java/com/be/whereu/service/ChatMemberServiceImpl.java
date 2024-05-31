package com.be.whereu.service;


import com.be.whereu.model.dto.ChatMemberDto;
import com.be.whereu.model.entity.ChatMemberEntity;
import com.be.whereu.repository.ChatMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatMemberServiceImpl implements ChatMemberService {
    private final ChatMemberRepository chatMemberRepository;


    @Override
    public boolean insert(ChatMemberDto dto) {
//        ChatMemberEntity.toEntity(ChatMemberDto)
//        return chatMemberRepository.save();
        return false;
    }
}
