package com.be.whereu.service;


import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {
    public boolean createChat(Long memberId);
    public void addMemberChat(Long memberId, Long chatId);
    public boolean exitChat(Long memberId,Long chatId);

}
