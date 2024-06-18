package com.be.whereu.model.dto.board;

import com.be.whereu.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardDetailsListDto {
    private Long postId;
    private String nick;
    private String title;
    private String content;
    private String createDate;
    private Long commentCount;
    private Long likeCount;
    private Boolean isLiked;

    public BoardDetailsListDto(Long postId, String nick, String title, String content, LocalDateTime createDate, Long commentCount, Long likeCount, Boolean isLiked ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.postId = postId;
        this.nick = nick;
        this.title = title;
        this.content = content;
        this.createDate = createDate.format(formatter);
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;

    }

 }
