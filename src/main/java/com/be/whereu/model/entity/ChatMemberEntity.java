package com.be.whereu.model.entity;

import com.be.whereu.model.dto.ChatMemberDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_member_tbl")
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


