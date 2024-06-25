package com.be.whereu.model.entity;


import com.be.whereu.model.Rtype;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private List<ChatMemberGroupEntity> chatMembers;
    @OneToMany(mappedBy = "chat" ,cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<MessageEntity> message;


}
