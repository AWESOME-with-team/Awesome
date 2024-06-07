package com.be.whereu.repository;

import com.be.whereu.model.entity.ChatMemberEntity;
import com.be.whereu.model.entity.MemberGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity,Long> {
    Optional<ChatMemberEntity> findByMemberIdAndChatId(Long memberId, Long chatId);

    @Query("SELECT cm FROM ChatMemberEntity cm JOIN FETCH cm.chat where cm.member.id= :memberId")
    Optional<List<ChatMemberEntity>> findListWithChatByMemberId(Long memberId);
}
