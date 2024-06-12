package com.be.whereu.model.dto.board;

import com.be.whereu.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
public class CommentRequestDto {
    private Long id;
    private Long postId;
    private String content;
    private Long parentId;


}