package com.be.whereu.model.dto.board;

import com.be.whereu.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentListResponseDto {
    private Long id;
    private String nick;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
    private Long parentId;
    private String createDate;
    private List<CommentListResponseDto> children;
    private String profile;


    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public int getLikeCount() {
        return likeCount != null ? likeCount : 0;
    }
    public int getReplyCount() {
        return replyCount != null ? replyCount : 0; // null 체크
    }

    public CommentListResponseDto(Long id, String nick, String content, Long likeCount,  Long parentId, LocalDateTime createDate, String profile) {
        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.id = id;
        this.nick = nick;
        this.content = content;
        this.likeCount = Math.toIntExact(likeCount);
        this.parentId = parentId;
        this.createDate = createDate.format(formatter);
        this.profile = profile;
    }



    public static CommentListResponseDto toDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                .id(commentEntity.getId())
                .nick(commentEntity.getMember().getNick())
                .content(commentEntity.getContent())
                .createDate(commentEntity.getCreateAt().format(formatter))
                .parentId(commentEntity.getParent() != null ? commentEntity.getParent().getId() : null)
                .build();




        return commentListResponseDto;
    }


}
