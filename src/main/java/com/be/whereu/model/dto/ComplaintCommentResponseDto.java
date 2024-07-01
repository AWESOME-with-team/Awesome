package com.be.whereu.model.dto;

import com.be.whereu.model.entity.ComplaintEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintCommentResponseDto {
    private Long id;
    private Long commentId;
    private String writerByComment;
    private String nickByComplaint;
    private String reason;
    private String createDate;

    public static ComplaintCommentResponseDto toDto(ComplaintEntity entity) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return ComplaintCommentResponseDto.builder()
                .id(entity.getId())
                .commentId(entity.getComment() != null ? entity.getComment().getId() : null)
                .reason(entity.getReason())
                .createDate(entity.getCreateAt().format(formatter))
                .build();
    }
}
