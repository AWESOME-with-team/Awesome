package com.be.whereu.model.dto;

import com.be.whereu.model.entity.GroupRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequestDto {
    private Long chatId;
    private List<String> nickname;

    public static GroupRequestDto toDto(GroupRequestEntity groupRequestEntity) {
        return GroupRequestDto.builder()
                .chatId(groupRequestEntity.getId())
                .nickname(Collections.singletonList(groupRequestEntity.getMember().getNick()))
                .build();
    }
}