package com.be.whereu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintPostRequestDto {
    private Long id;
    private Long postId;
    private String reason;
}
