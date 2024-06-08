package com.be.whereu.service;


public interface ChatService {
    public boolean createChatWithGroup(Long groupId);
    public void addMemberChat(Long memberId, Long chatId);
    public boolean exitChat(Long memberId,Long chatId);
    //public List<ChatDto> getChatRooms();


}
