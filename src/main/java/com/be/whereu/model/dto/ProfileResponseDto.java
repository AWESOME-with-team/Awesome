package com.be.whereu.model.dto;

import com.be.whereu.model.dto.board.CommentResponseDto;
import com.be.whereu.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileResponseDto {
    private String nick;
    private String email;
    private String universityEmail;
    private String universityName;
    private String profile;

    public ProfileResponseDto toDto(MemberEntity entity) {
        return ProfileResponseDto.builder()
                .nick(entity.getNick())
                .email(entity.getEmail())
                .universityEmail(entity.getUniversityEmail())
                .universityName(entity.getUniversityName())
                .profile(entity.getProfile())
                .build();
    }
}
