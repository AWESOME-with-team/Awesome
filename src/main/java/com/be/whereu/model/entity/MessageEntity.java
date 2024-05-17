package com.be.whereu.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
