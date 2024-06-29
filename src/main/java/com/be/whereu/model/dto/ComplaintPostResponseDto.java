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
public class ComplaintPostResponseDto {
    private Long id;
    private Long postId;
    private String writerByPost;
    private String nickByComplaint;
    private String reason;
    private String createDate;

    public static ComplaintPostResponseDto toDto(ComplaintEntity entity) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return ComplaintPostResponseDto.builder()
                .id(entity.getId())
                .postId(entity.getPost() != null ? entity.getPost().getId() : null)
                .reason(entity.getReason())
                .createDate(entity.getCreateAt().format(formatter))
                .build();
    }
}
