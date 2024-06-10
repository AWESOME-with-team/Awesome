package com.be.whereu.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.be.whereu.controller.ChatListDto;
import com.be.whereu.exception.CustomServiceException;
import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.Rtype;
import com.be.whereu.model.dto.ChatDto;
import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.ChatMemberEntity;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.*;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final GroupRepository groupRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;
    private final SecurityContextManager securityContextManager;

    /**
     *  dm chatting 방 생성
     *
     */
    @Transactional
    @Override  //nickName으로 memberId를 조회하기로 바꾸기
    public boolean createChatWithGroup(Long groupId){
        try {
            Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());
            ChatEntity chatEntity=new ChatEntity();
            chatEntity.setRtype(Rtype.group);
            chatRepository.save(chatEntity);

            MemberEntity memberEntity=new MemberEntity();
            memberEntity.setId(memberId);

            GroupEntity groupEntity= new GroupEntity();
            groupEntity.setId(groupId);


            ChatMemberEntity chatMemberEntity=new ChatMemberEntity();
            chatMemberEntity.setMember(memberEntity);
            chatMemberEntity.setGroup(groupEntity);
            chatMemberEntity.setChat(chatEntity);
            chatMemberRepository.save(chatMemberEntity);

            log.info("chatMember: {}",chatMemberEntity.toString());
            return true;
        }catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error creating chat: {} ", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("createChat occurred {}", e.getMessage());
            return false;
        }


    }


    /**
     * 기존 채팅방 인원 초대
     */
    @Transactional
    @Override
    public void addMemberChat(Long memberId, Long chatId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow();
        ChatMemberEntity chatMember = new ChatMemberEntity();
        chatMember.setMember(member);
        chatMember.setChat(chat);
        chatMemberRepository.save(chatMember);

    }
    @Transactional
    @Override
    public boolean exitChat(Long memberId, Long chatId) {

        // ChatMemberEntity 찾기
        // Entity 삭제
        Optional<ChatMemberEntity> chatMemberOptional =chatMemberRepository.findByMemberIdAndChatId(memberId, chatId);

        if (chatMemberOptional.isPresent()) {
            // Entity 삭제
            chatMemberRepository.delete(chatMemberOptional.get());
            return true;  // 성공적으로 삭제됨
        }
        return false;  // 해당 멤버가 채팅방에 없음

    }




}








