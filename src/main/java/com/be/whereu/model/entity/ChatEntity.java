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
    private List<MessageEntity> message;

    public String getLastMessage() {
        Optional<String> lastMessage = message.stream()
                .map(MessageEntity::getContent)
                .reduce((first, second) -> second); // 마지막 요소만 남기기 위해 reduce 사용
        return lastMessage.orElse(null); // 값이 없으면 null 반환
    }
    public String getLastMessageTime() {
        Optional<LocalDateTime> lastMessageTime = message.stream()
                .map(MessageEntity::getCreateAt)
                .max(LocalDateTime::compareTo);
        return lastMessageTime.map(LocalDateTime::toString).orElse(null); // 값이 없으면 null 반환
    }

}
