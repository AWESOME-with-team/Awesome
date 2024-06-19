package com.be.whereu.model.dto.board;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryDto {
    private Long categoryId;
    private String boardName;
}
