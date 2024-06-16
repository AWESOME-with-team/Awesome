package com.be.whereu.model.dto.board;


import lombok.Data;

@Data
public class PostRequestDto {
    private Long id;
    private Long commonId;
    private String title;
    private String content;


}
