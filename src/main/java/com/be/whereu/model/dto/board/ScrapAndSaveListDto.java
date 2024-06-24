package com.be.whereu.model.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScrapAndSaveListDto {
    private Long postId;
    private String boardName;
    private String nick;
    private String title;
    private String content;
    private String createDate;
    private Long commentCount;
    private Long likeCount;
    private Integer viewCount;
    private Boolean isLiked;
    private Boolean isScrap;


    public ScrapAndSaveListDto(Long postId, String boardName, String nick, String title, String content, LocalDateTime createDate, Long commentCount, Long likeCount, Integer viewCount, Boolean isLiked, Boolean isScrap ){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.postId = postId;
        this.boardName = boardName;
        this.nick = nick;
        this.title = title;
        this.content = content;
        this.createDate = createDate.format(formatter);
        this.commentCount = commentCount;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.viewCount = viewCount != null ? viewCount : 0;
        this.isLiked = isLiked;
        this.isScrap = isScrap;



    }
}
