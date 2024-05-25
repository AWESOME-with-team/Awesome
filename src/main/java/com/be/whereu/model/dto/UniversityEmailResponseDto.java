package com.be.whereu.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UniversityEmailResponseDto {
    Integer status;
    Integer code;
    Boolean success;
    String message;
}
