package com.be.whereu.model.dto;

import com.be.whereu.model.entity.ChatMemberGroupEntity;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMemberDto {
    private Long id;
    private Long memberId;
    private Long chatId;



    public static ChatMemberDto toDto(ChatMemberGroupEntity entity) {
        if (entity == null) {
            return null;
        }
        return ChatMemberDto.builder()
                .id(entity.getId())
                .memberId(entity.getMember().getId())
                .chatId(entity.getChat().getId())
                .build();
    }
}