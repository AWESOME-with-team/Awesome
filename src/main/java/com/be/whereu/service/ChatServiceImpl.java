package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.Rtype;
import com.be.whereu.model.dto.ChatListDto;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberGroupRepository chatMemberGroupRepository;
    private final SecurityContextManager securityContextManager;

    /**
     *  group 생성시 chat 방 생성
     *
     */
    @Transactional
    @Override  //nick으로 memberId를 조회하기로 바꾸기
    public void createChatByGroup(Long groupId){
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

            chatMemberGroupRepository.save(chatMemberEntity);


        }catch (DataAccessException e) {
            // 데이터베이스 관련 예외 처리
            log.error("Database error creating chat: {} ", e.getMessage());
        }catch (NumberFormatException e) {
            log.error("Invalid member ID format: {}", e.getMessage());
        }catch (IllegalArgumentException e) {
            log.error("Entity not found: {}", e.getMessage());
        }catch (Exception e) {
            log.error("createChat occurred {}", e.getMessage());
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
        chatMemberGroupRepository.save(chatMember);

    }
    @Transactional
    @Override
    public boolean exitChat(Long memberId, Long chatId) {

        // ChatMemberEntity 찾기
        // Entity 삭제
        Optional<ChatMemberEntity> chatMemberOptional = chatMemberGroupRepository.findByMemberIdAndChatId(memberId, chatId);

        if (chatMemberOptional.isPresent()) {
            // Entity 삭제
            chatMemberGroupRepository.delete(chatMemberOptional.get());
            return true;  // 성공적으로 삭제됨
        }
        return false;  // 해당 멤버가 채팅방에 없음

    }

    @Override
    public List<ChatListDto> getChatList() {
        Long memberId = Long.parseLong(securityContextManager.getAuthenticatedUserName());

        return chatMemberGroupRepository.findChatListByMemberId(memberId).orElseThrow(
                () -> new ResourceNotFoundException("No chat member found for id " + memberId)
        );
    }

}