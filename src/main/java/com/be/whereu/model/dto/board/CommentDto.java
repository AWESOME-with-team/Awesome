package com.be.whereu.model.dto.board;

import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {   //CommentRequestDto , CommentResponseDto
    private Long id;
    private PostDto post;
    private MemberDto member; //x
    private String content;
    private Integer likeCount; //x
    private CommentEntity parent;
    List<CommentEntity> children; //x      // co

    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Integer getLikeCount(){
        return likeCount !=null ? likeCount : 0;
    }


    public static CommentDto toDto(CommentEntity commentEntity) {
        MemberDto memberDto = MemberDto.toDto(commentEntity.getMember());
        PostDto postDto = PostDto.toDto(commentEntity.getPost());
        return CommentDto.builder()
                .id(commentEntity.getId())
                .post(postDto)
                .member(memberDto)
                .content(commentEntity.getContent())
                .parent(commentEntity.getParent())
                .children(commentEntity.getChildren())
                .likeCount(commentEntity.getLikeCount())
                .build();
    }
}
