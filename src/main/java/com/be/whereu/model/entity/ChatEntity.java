package com.be.whereu.model.entity;


import com.be.whereu.model.Rtype;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_tbl")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Rtype rtype;
    @OneToMany(mappedBy = "chat")
    private List<ChatMemberEntity> chatMembers;
    @OneToMany(mappedBy = "chat")
    private List<MessageEntity> message;

}
