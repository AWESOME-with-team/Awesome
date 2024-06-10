package com.be.whereu.repository;

import com.be.whereu.model.dto.ChatListDto;
import com.be.whereu.model.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMemberGroupRepository extends JpaRepository<ChatMemberEntity,Long> {
    Optional<ChatMemberEntity> findByMemberIdAndChatId(Long memberId, Long chatId);

    @Query("SELECT new com.be.whereu.model.dto.ChatListDto(c.id, " +
            "CASE WHEN c.rtype = com.be.whereu.model.Rtype.group THEN g.groupName ELSE m.nick END, " +
            "latestMessage.content, " +
            "c.rtype, COUNT(cm.id), MAX(latestMessage.createAt)) " +
            "FROM ChatEntity c " +
            "JOIN c.chatMembers cm " +
            "LEFT JOIN cm.group g " +
            "LEFT JOIN cm.member m " +
            "LEFT JOIN MessageEntity latestMessage ON latestMessage.id = (SELECT m.id FROM MessageEntity m WHERE m.chat.id = c.id ORDER BY m.createAt DESC) " +
            "WHERE cm.member.id = :memberId " +
            "GROUP BY c.id, g.groupName, m.nick, c.rtype, latestMessage.content")
    Optional<List<ChatListDto>> findChatListByMemberId(@Param("memberId") Long memberId);
}
