package com.be.whereu.service;



import com.be.whereu.model.dto.ChatListDto;


import java.util.List;

public interface ChatService {
    public void createChatByGroup(Long groupId);
    public void addMemberGroupChat(Long memberId, Long groupId);
    public boolean exitChat(Long memberId,Long chatId);
    public List<ChatListDto> getChatList();



}
