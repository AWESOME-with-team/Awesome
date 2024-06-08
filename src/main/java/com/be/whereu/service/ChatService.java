package com.be.whereu.service;


import com.be.whereu.controller.ChatListDto;
import com.be.whereu.model.dto.ChatDto;

import java.util.List;

public interface ChatService {
    public boolean createChatWithGroup(Long groupId);
    public void addMemberChat(Long memberId, Long chatId);
    public boolean exitChat(Long memberId,Long chatId);
    public List<ChatListDto> getChatRooms();


}
