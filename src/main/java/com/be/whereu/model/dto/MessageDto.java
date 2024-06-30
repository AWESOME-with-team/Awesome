package com.be.whereu.model.dto;

import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.MemberEntity;
import com.be.whereu.model.entity.MessageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Builder
@Data
public class MessageDto {
    private Long id;
    private String content;
    private String createAt;
    private String nick;


    public static MessageDto toDto(MessageEntity messageEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String createTime=messageEntity.getCreateAt().format(formatter);
        return MessageDto.builder()
                .id(messageEntity.getId())
                .content(messageEntity.getContent())
                .createAt(createTime)
                .nick(messageEntity.getMember()==null? "존재하지 않는 사용자" :messageEntity.getMember().getNick()).build();
    }
}
