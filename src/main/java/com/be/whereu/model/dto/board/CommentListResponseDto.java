package com.be.whereu.model.dto.board;

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
public class CommentListResponseDto {
    private Long id;
    private Long postId;
    private Long memberId;
    private String nick;
    private String universityName;
    private String content;
    private Long likeCount;
    private Long parentId;
    private String createDate;
    private String modifyDate;
    private List<CommentListResponseDto> children;


    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Long getLikeCount() {
        return likeCount != null ? likeCount : 0;
    }

    public CommentListResponseDto(Long id, Long postId, Long memberId, String nick, String universityName, String content, Long likeCount, Long parentId, LocalDateTime createDate, LocalDateTime modifyDate) {
        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.id = id;
        this.postId = postId;
        this.memberId = memberId;
        this.nick = nick;
        this.universityName = universityName;
        this.content = content;
        this.likeCount = likeCount;
        this.parentId = parentId;
        this.createDate = createDate.format(formatter);
        this.modifyDate = modifyDate.format(formatter);
    }



    public static CommentListResponseDto toDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                .id(commentEntity.getId())
                .postId(commentEntity.getPost().getId())
                .memberId(commentEntity.getMember().getId())
                .nick(commentEntity.getMember().getNick())
                .universityName(commentEntity.getMember().getUniversityName())
                .content(commentEntity.getContent())
                .createDate(commentEntity.getCreateAt().format(formatter))
                .modifyDate(commentEntity.getModifiedAt().format(formatter))
                .parentId(commentEntity.getParent() != null ? commentEntity.getParent().getId() : null)
                .build();



        return commentListResponseDto;
    }


}
