package com.be.whereu.controller;

import com.be.whereu.model.ChatMessage;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.ChatMemberRepository;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.MessageRepository;
import com.be.whereu.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller

public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatService chatService;


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
    @PostMapping("/api/chat/create")
    public ResponseEntity<Void> createWithGroup(Long groupId){
        chatService.createChatWithGroup(groupId);
        return ResponseEntity.ok().build();
    }



    //채팅방 초대 한명씩 추가 하는 api필요
    @ResponseBody
    @PostMapping("/api/chat/member")
    public ResponseEntity<Void> addMemberChat(Long memberId, Long chatId){
        chatService.addMemberChat(memberId,chatId);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @DeleteMapping("/api/chat/member")
    public ResponseEntity<Void> exitChat(Long memberId, Long chatId){
        boolean isSuccess=chatService.exitChat(memberId,chatId);
        log.info("isSuccess {}",isSuccess);
        return ResponseEntity.ok().build();
    }

//    //본인 memberId가 속한 채팅방 List 가져오기
//    @ResponseBody
//    @GetMapping("/list")
//    public ResponseEntity<List<ChatDto>> listChat(){
//        List<ChatDto> chatList= chatService.getChatRooms();
//        return ResponseEntity.ok(chatList);
//    }




}
