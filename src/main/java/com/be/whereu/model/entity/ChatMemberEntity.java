package com.be.whereu.model.entity;

import com.be.whereu.model.dto.ChatMemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_mg_tbl")
public class ChatMemberEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",foreignKey = @ForeignKey(name="member_chat_fk"))
    private MemberEntity member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_id",foreignKey = @ForeignKey(name="chat_member_fk"))
    private ChatEntity chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id",foreignKey = @ForeignKey(name="chat_group_fk"))
    private GroupEntity group;


    public static ChatMemberEntity toEntity(ChatMemberDto dto, MemberEntity member, ChatEntity chat) {
        if (dto == null) {
            return null;
        }
        return ChatMemberEntity.builder()
                .id(dto.getId())
                .member(member)
                .chat(chat)
                .build();
    }
}


