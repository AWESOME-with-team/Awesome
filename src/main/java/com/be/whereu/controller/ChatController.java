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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
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
    @PostMapping("/chat/dm/create")
    public ResponseEntity<Void> createDmChat(Long memberId){
        chatService.createChat(memberId);
        return ResponseEntity.ok().build();
    }



    //채팅방 초대 한명씩 추가 하는 api필요
    @ResponseBody
    @PostMapping("/chat/member/add")
    public ResponseEntity<Void> addMemberChat(Long memberId, Long chatId){
        chatService.addMemberChat(memberId,chatId);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @DeleteMapping("/chat/exit")
    public ResponseEntity<Void> exitChat(Long memberId, Long chatId){
        boolean isSuccess=chatService.exitChat(memberId,chatId);
        log.info("isSuccess {}",isSuccess);
        return ResponseEntity.ok().build();
    }





}
