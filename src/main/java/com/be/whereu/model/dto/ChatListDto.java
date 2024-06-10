package com.be.whereu.model.dto;

import com.be.whereu.model.Rtype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatListDto {
    private Long chatId;
    private String chatName;
    private String content;
    private Rtype chatType;
    private int count; // 채팅방 참여자 수
    private String lastChatAt; // 가장 최근 메시지 시간

    public ChatListDto(Long chatId, String chatName, String content, Rtype chatType, Long count, LocalDateTime lastChatAt) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.content = content;
        this.chatType = chatType;
        this.count = count.intValue();
        // LocalDateTime을 String으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.lastChatAt = lastChatAt != null ? lastChatAt.format(formatter) : null;
    }
}
