package com.be.whereu.model.entity;

import com.be.whereu.model.dto.MessageDto;
import jakarta.persistence.*;
import lombok.*;

import javax.sound.midi.MetaMessage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_tbl" , indexes=@Index(name="idx_chat_id", columnList = "chat_id"))
public class MessageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name="message_member_fk"))
    private MemberEntity member;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_id", foreignKey = @ForeignKey(name="message_chat_fk"))
    private ChatEntity chat;

    public static MessageEntity toEntity(MessageDto dto , Long memberId ,Long chatId ) {
        MemberEntity memberEntity=new MemberEntity();
        ChatEntity chatEntity= new ChatEntity();
        memberEntity.setId(memberId);
        chatEntity.setId(chatId);
        return  MessageEntity.builder()
                .chat(chatEntity)
                .member(memberEntity)
                .content(dto.getContent())
                .build();
    }
}
