package com.be.whereu.model.dto.board;


import com.be.whereu.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardListDto {
    Long id;
    String title;
    String lastPostTitle;

    public static BoardListDto fromEntity(PostEntity entity) {
        return BoardListDto.builder()
                .id(entity.getCommon().getCodeId())
                .title(entity.getCommon().getCodeName())
                .lastPostTitle(entity.getTitle())
                .build();
    }
}


