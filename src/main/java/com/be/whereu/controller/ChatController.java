package com.be.whereu.controller;

import com.be.whereu.model.ChatMessage;
import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.ChatMemberRepository;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;
    



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
//        ChatEntity chat = chatRepository.findById(Long.parseLong(chatMessage.getChatId())).orElseThrow();
//
//        MessageEntity message = new MessageEntity();
//        message.setChat(chat);
//        message.setContent(chatMessage.getContent());
//        // message.setMember(...) // 현재 사용자를 설정해야 합니다.
//
//        messageRepository.save(message);

        // 누가보냈는지 식별하기
//        memberRepository.findby
        var sender = memberRepository.findByEmail(chatMessage.getSender());

        // chatMemberRepository


        // sender가 chat 에 해당되는지 검증

        var chat = chatRepository.findById(Long.parseLong(chatMessage.getChatId()))
                .orElseThrow();
        var message = MessageEntity.builder()
                .member(sender)
                .content(chatMessage.getContent())
                .chat(chat)
                .build();
        messageRepository.save(message);
        return chatMessage;
    }
}
