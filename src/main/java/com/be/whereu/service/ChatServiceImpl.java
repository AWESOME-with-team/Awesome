package com.be.whereu.service;

import com.be.whereu.model.Rtype;
import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.ChatMemberEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final GroupRepository groupRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;

    /**
     *  dm chatting 방 생성
     *
     */
    @Transactional
    @Override
    public void createDmChat(Long receiverId, Authentication authentication){
        Long memberId = Long.parseLong(authentication.getName());
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        MemberEntity member2 = memberRepository.findById(receiverId).orElseThrow();

        // 채팅방 생성
        ChatEntity chat = new ChatEntity();
        chat.setRtype(Rtype.dm);
        chatRepository.save(chat);

        // member1 추가
        ChatMemberEntity chatMember = new ChatMemberEntity();
        chatMember.setMember(member);
        chatMember.setChat(chat);
        chatMemberRepository.save(chatMember);

        // member2 추가
        ChatMemberEntity chatMember2 = new ChatMemberEntity();
        chatMember2.setMember(member2);
        chatMember2.setChat(chat);
        chatMemberRepository.save(chatMember2);

    }

    /**
     * group chatting 방생성
     *
     */
    @Transactional
    @Override
    public void createGroupChat(List<Long> memberIds, Authentication authentication){
        Long ownerId = Long.parseLong(authentication.getName());
        MemberEntity owner = memberRepository.findById(ownerId).orElseThrow();

        // 채팅방 생성
        ChatEntity chat = new ChatEntity();
        chat.setRtype(Rtype.group);
        chatRepository.save(chat);

        // owner 추가
        ChatMemberEntity chatMember2 = new ChatMemberEntity();
        chatMember2.setMember(owner);
        chatMember2.setChat(chat);
        chatMemberRepository.save(chatMember2);

        // group원들 추가
        for(Long memberId : memberIds){
            //memberId로 member가져오기
            MemberEntity member = memberRepository.findById(memberId).orElseThrow();

            //반복문을 통해 chatMember db에 값을 넣어 준다.
            ChatMemberEntity chatMember = new ChatMemberEntity();
            chatMember.setMember(member);
            chatMember.setChat(chat);
            chatMemberRepository.save(chatMember);
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





