package com.be.whereu.service;


import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {
    public void createDmChat(Long memberId, Authentication authentication);
    public void createGroupChat(List<Long> memberIds, Authentication authentication);
    public void addMemberChat(Long memberId, Long chatId);
    public boolean exitChat(Long memberId,Long chatId);

}
