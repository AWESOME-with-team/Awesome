package com.be.whereu.model.dto;

import com.be.whereu.model.Rtype;
import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.MessageEntity;
import lombok.AllArgsConstructor;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDto {
    private Long id;
    private Rtype rtype;
    List<MessageDto> message;


    public  static  ChatDto toDto(ChatEntity entity) {
        return ChatDto.builder()
                .id(entity.getId())
                .rtype(entity.getRtype())
                .build();

    }
}
