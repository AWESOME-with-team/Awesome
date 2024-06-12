package com.be.whereu.model.dto.board;

import com.be.whereu.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostResponseDto {
    private Long id;
    private Long commonId;  //게시판 번호
    private String title;
    private String content;
    private Integer likeCount;  // 좋아요를 누르면 +1 다시누르면 -1
    private Integer viewCount; // 접속할 때 viewCount를  1씩 올려주는 api
    private String nick;
    private String createDate;
    private String modifyDate;
    private String School;






    public void CreatedDate(LocalDateTime CreatedDate){
        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.createDate= CreatedDate.format(formatter);
    }
    public void ModifiedDate(LocalDateTime ModifiedDate){
        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.modifyDate= ModifiedDate.format(formatter);
    }

    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Integer getViewCount(){
        return viewCount !=null ? viewCount : 0;
    }

    public Integer getLikeCount(){
        return likeCount !=null ? likeCount : 0;
    }


    public static PostResponseDto toDto(PostEntity entity) {
        // LocalDate to String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return PostResponseDto.builder()
                .id(entity.getId())
                .commonId(entity.getCommon().getCodeId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .likeCount(entity.getLikeCount())
                .viewCount(entity.getViewCount())
                .nick(entity.getMember().getNick())
                .createDate(entity.getCreateAt().format(formatter))
                .modifyDate(entity.getModifiedAt().format(formatter))
                .School(entity.getMember().getUniversityName())
                .build();
    }

}
