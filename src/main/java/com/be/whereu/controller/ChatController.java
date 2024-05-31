package com.be.whereu.controller;

import com.be.whereu.model.ChatMessage;
import com.be.whereu.model.entity.ChatMemberEntity;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.ChatMemberRepository;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.MessageRepository;
import com.be.whereu.service.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatServiceImpl chatService;


    /*
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    */
    @MessageMapping("/{chatId}")
    @SendTo("/room/{chatId}")                                   //memberId도 받아서 중간엔티티조횔
    public ChatMessage sendMessage( @DestinationVariable("chatId") Long chatId,  ChatMessage chatMessage, Authentication authentication) {


        Long memberId = Long.parseLong(authentication.getName());
        var chat = chatRepository.findById(Long.parseLong(chatMessage.getChatId()))
                .orElseThrow();

        // 누가보냈는지 식별하기
        var sender = memberRepository.findById(memberId).orElseThrow();

        // chatMemberRepository
        ChatMemberEntity entity= ChatMemberEntity.builder()
                .member(sender)
                .chat(chat)
                .build();
        chatMemberRepository.save(entity);




        // sender가 chat 에 해당되는지 검증
        var message = MessageEntity.builder()
                .member(sender)
                .content(chatMessage.getContent())
                .chat(chat)
                .build();
        messageRepository.save(message);
        return chatMessage;
    }


    @ResponseBody
    @PostMapping("/create/dm/chat")
    public ResponseEntity<Void> createDmChat(Long memberId, Authentication authentication){
        chatService.createDmChat(memberId,authentication);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/create/group/chat")
    public ResponseEntity<Void> createGroupChat(List<Long> memberIds, Authentication authentication){
        chatService.createGroupChat(memberIds,authentication);
        return ResponseEntity.ok().build();
    }


//    @MessageMapping("/createDM/{receiverId}")
//    @SendTo("/room/newChat")
//    public ChatMessage createDMChat(@DestinationVariable("receiverId") Long receiverId, ChatMessage chatMessage, Authentication authentication) {
//        // 새로운 DM 채팅방 생성 및 첫 메시지 전송 로직
//        Long chatId = chatService.createDMChat(receiverId, authentication, chatMessage);
//
//        // chatMessage에 새로운 chatId 설정
//        chatMessage.setChatId(String.valueOf(chatId));
//
//        return chatMessage;
//    }

}
