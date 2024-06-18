package com.be.whereu.model.dto.board;


import com.be.whereu.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private Long id;
    private String content;


    public static CommentResponseDto toDto(CommentEntity entity) {
        return CommentResponseDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .build();
    }
}





