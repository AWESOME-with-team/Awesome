package com.be.whereu.service;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {
    public void createDmChat(Long receiverId, Authentication authentication);
    public void createGroupChat(List<Long> memberIds, Authentication authentication);
}
