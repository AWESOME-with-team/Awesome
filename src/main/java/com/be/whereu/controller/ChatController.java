package com.be.whereu.controller;
import com.be.whereu.model.ChatMessage;
import com.be.whereu.model.dto.ChatListDto;
import com.be.whereu.model.dto.MessageDto;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MessageEntity;
import com.be.whereu.repository.ChatRepository;
import com.be.whereu.repository.MemberRepository;
import com.be.whereu.repository.MessageRepository;
import com.be.whereu.security.authentication.SecurityContextManager;
import com.be.whereu.service.ChatService;
import com.be.whereu.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final SecurityContextManager securityContextManager;


    /**
     *
     * @return chat 방의 목록이 나옵니다
     */
    @GetMapping("/list")
    public ResponseEntity<List<ChatListDto>> getChatList(){
       List<ChatListDto> chatList= chatService.getChatList();
       return ResponseEntity.ok().body(chatList);
    }

    /**
     *
     * @param chatId 채팅방id
     * @return 채팅방의 id에 따른 대화내용
     */
    @GetMapping("/message/{chatId}")
    public ResponseEntity<List<MessageDto>> getMessageList(@PathVariable Long chatId){
        return ResponseEntity.ok().body(messageService.getMessageList(chatId));
    }

    /**
     *
     * @param chatId
     * @param dto MessageDto
     * @param authentication
     * @return 채팅방에 소켓통신을 위한 메소드
     */
    @MessageMapping("/{chatId}")
    @SendTo("/room/{chatId}")
    public MessageDto sendMessage(@DestinationVariable("chatId") Long chatId, MessageDto dto, Authentication authentication) {
        Long memberId = Long.parseLong(authentication.getName());

        return messageService.messageSave(dto,memberId,chatId);
    }



    //채팅방 초대 한명씩 추가 하는 api필요
    @ResponseBody
    @PostMapping("/member")
    public ResponseEntity<Void> addMemberChat(Long memberId, Long chatId){
        chatService.addMemberChat(memberId,chatId);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @DeleteMapping("/member")
    public ResponseEntity<Void> exitChat(Long memberId, Long chatId){
        boolean isSuccess=chatService.exitChat(memberId,chatId);
        log.info("isSuccess {}",isSuccess);
        return ResponseEntity.ok().build();
    }




}
