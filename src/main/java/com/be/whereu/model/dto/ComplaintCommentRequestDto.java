package com.be.whereu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintCommentRequestDto {
    private Long id;
    private Long commentId;
    private String reason;
}
