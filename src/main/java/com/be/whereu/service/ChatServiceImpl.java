package com.be.whereu.service;

import com.be.whereu.exception.ResourceNotFoundException;
import com.be.whereu.model.Rtype;
import com.be.whereu.model.dto.ChatListDto;
import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.ChatMemberGroupEntity;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.*;
import com.be.whereu.security.authentication.SecurityContextManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    @Override
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


            ChatMemberGroupEntity chatMemberGroupEntity =new ChatMemberGroupEntity();
            chatMemberGroupEntity.setMember(memberEntity);
            chatMemberGroupEntity.setGroup(groupEntity);
            chatMemberGroupEntity.setChat(chatEntity);

            chatMemberGroupRepository.save(chatMemberGroupEntity);


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
    public void addMemberGroupChat(Long memberId, Long groupId) {

        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setId(memberId);
        GroupEntity groupEntity= new GroupEntity();
        groupEntity.setId(groupId);
        List<ChatMemberGroupEntity> chatMemberGroupEntity=chatMemberGroupRepository.findByGroupId(groupId).orElseThrow(
                () -> new ResourceNotFoundException("Chat member group not found")
        );
        ChatEntity chat=chatMemberGroupEntity.stream().map(ChatMemberGroupEntity::getChat).findAny().get();
        System.out.println(chat.getId());
        ChatMemberGroupEntity chatMember = new ChatMemberGroupEntity();
        chatMember.setMember(memberEntity);
        chatMember.setGroup(groupEntity);
        chatMember.setChat(chat);
        chatMemberGroupRepository.save(chatMember);

    }

    @Transactional
    @Override
    public boolean exitChat(Long memberId, Long chatId) {

        // ChatMemberEntity 찾기
        // Entity 삭제
        Optional<ChatMemberGroupEntity> chatMemberOptional = chatMemberGroupRepository.findByMemberIdAndChatId(memberId, chatId);

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


        List<ChatListDto> result =chatMemberGroupRepository.findChatListByMemberId(memberId).orElseThrow(
                () -> new ResourceNotFoundException("No chat member found for id " + memberId)
        );
        for(ChatListDto dto:result){
            System.out.println(dto.getLastChatAt());
        };
        return result.stream()
                .sorted((chat1, chat2) -> {
                    if (chat1.getLastChatAt() == null && chat2.getLastChatAt() == null) {
                        return 0;
                    } else if (chat1.getLastChatAt() == null) {
                        return 1; // chat1의 lastChatAt이 null이면 chat1을 chat2보다 뒤로 보낸다
                    } else if (chat2.getLastChatAt() == null) {
                        return -1; // chat2의 lastChatAt이 null이면 chat2를 chat1보다 뒤로 보낸다
                    } else {
                        return chat2.getLastChatAt().compareTo(chat1.getLastChatAt());
                    }
                })
                .toList();
    }

}