package com.be.whereu.repository;

import com.be.whereu.model.dto.ChatListDto;
import com.be.whereu.model.entity.ChatMemberGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMemberGroupRepository extends JpaRepository<ChatMemberGroupEntity,Long> {
    Optional<ChatMemberGroupEntity> findByMemberIdAndChatId(Long memberId, Long chatId);

    @Query("SELECT new com.be.whereu.model.dto.ChatListDto(c.id, " +
            "CASE WHEN c.rtype = com.be.whereu.model.Rtype.group THEN g.groupName ELSE m.nick END, " +
            "latestMessage.content, " +
            "c.rtype, COUNT(cm.id), MAX(latestMessage.createAt)) " +
            "FROM ChatEntity c " +
            "JOIN c.chatMembers cm " +
            "LEFT JOIN cm.group g " +
            "LEFT JOIN cm.member m " +
            "LEFT JOIN MessageEntity latestMessage ON latestMessage.chat.id = c.id AND latestMessage.createAt = (SELECT MAX(m2.createAt) FROM MessageEntity m2 WHERE m2.chat.id = c.id) " +
            "WHERE cm.member.id = :memberId " +
            "GROUP BY c.id, g.groupName, m.nick, c.rtype, latestMessage.content")
    Optional<List<ChatListDto>> findChatListByMemberId(@Param("memberId") Long memberId);
}
