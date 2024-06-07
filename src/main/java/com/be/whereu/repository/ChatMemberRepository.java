package com.be.whereu.repository;

import com.be.whereu.model.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity,Long> {
    Optional<ChatMemberEntity> findByMemberIdAndChatId(Long memberId, Long chatId);
}
