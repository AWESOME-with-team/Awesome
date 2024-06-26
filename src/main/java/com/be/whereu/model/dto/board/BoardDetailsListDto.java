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
    private Integer viewCount;
    private Boolean isLiked;
    private Boolean isScrap;
    private String profile;

    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Integer getViewCount(){
        return viewCount !=null ? viewCount : 0;
    }

    public BoardDetailsListDto(Long postId, String nick, String title, String content, LocalDateTime createDate, Long commentCount, Long likeCount,  Integer viewCount, Boolean isLiked, Boolean isScrap, String profile ){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.postId = postId;
        this.nick = nick;
        this.title = title;
        this.content = content;
        this.createDate = createDate.format(formatter);
        this.commentCount = commentCount;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.viewCount = viewCount != null ? viewCount : 0;
        this.isLiked = isLiked;
        this.isScrap = isScrap;
        this.profile = profile;


    }

 }
