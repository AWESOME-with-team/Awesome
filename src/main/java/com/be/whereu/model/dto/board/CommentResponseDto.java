package com.be.whereu.model.dto.board;

import com.be.whereu.model.dto.MemberDto;
import com.be.whereu.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private MemberDto member;
    private String content;
    private Long likeCount;
    private Long parentId;
    private String createDate;
    private String modifyDate;
    private List<CommentResponseDto> children;


    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Long getLikeCount() {
        return likeCount != null ? likeCount : 0;
    }

    public CommentResponseDto(Long id, Long postId, MemberDto member, String content, Long likeCount, Long parentId, LocalDateTime createDate) {
        this.id = id;
        this.postId = postId;
        this.member = member;
        this.content = content;
        this.likeCount = likeCount;
        this.parentId = parentId;
        this.createDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }



    public static CommentResponseDto toDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(commentEntity.getId())
                .postId(commentEntity.getPost().getId())
                .member(MemberDto.toDto(commentEntity.getMember()))
                .content(commentEntity.getContent())
                //.likeCount(commentEntity.getLikeCount())
                .createDate(commentEntity.getCreateAt().format(formatter))
                .modifyDate(commentEntity.getModifiedAt().format(formatter))
                .parentId(commentEntity.getParent() != null ? commentEntity.getParent().getId() : null)
                .build();

        // Convert children comments
        if (commentEntity.getChildren() != null && !commentEntity.getChildren().isEmpty()) {
            commentResponseDto.setChildren(commentEntity.getChildren().stream()
                    .map(CommentResponseDto::toDto)
                    .collect(Collectors.toList()));
        }

        return commentResponseDto;
    }
}
