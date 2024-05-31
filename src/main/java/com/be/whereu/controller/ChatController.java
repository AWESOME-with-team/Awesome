package com.be.whereu.controller;

import com.be.whereu.model.ChatMessage;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.ChatMemberRepository;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    private final SimpMessagingTemplate messagingTemplate;


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

    @MessageMapping("/group/{chatId}/sendMessage")
    @SendTo("/topic/group/{chatId}")
    public ChatMessage sendMessage(@PathVariable Long chatId, ChatMessage chatMessage) {
        // 메시지를 데이터베이스에 저장
        var sender = memberRepository.findByEmail(chatMessage.getSender());
        var chat = chatRepository.findById(Long.valueOf(chatMessage.getChatId()))
                .orElseThrow();
        var message = MessageEntity.builder()
                .member(sender)
                .content(chatMessage.getContent())
                .chat(chat)
                .build();
        messageRepository.save(message);

        // 모든 그룹 멤버에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/group/" + chatId, chatMessage);

        return chatMessage;
    }
}
