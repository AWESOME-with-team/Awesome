package com.be.whereu.service;


import com.be.whereu.model.dto.ChatMemberDto;
import com.be.whereu.repository.ChatMemberGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatMemberServiceImpl implements ChatMemberService {
    private final ChatMemberGroupRepository chatMemberGroupRepository;


    @Override
    public boolean insert(ChatMemberDto dto) {
//        ChatMemberEntity.toEntity(ChatMemberDto)
//        return chatMemberRepository.save();
        return false;
    }
}
