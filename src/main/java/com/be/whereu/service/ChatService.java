package com.be.whereu.service;

import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final GroupRepository groupRepository;
}
