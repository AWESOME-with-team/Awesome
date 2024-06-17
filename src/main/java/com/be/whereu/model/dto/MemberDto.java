package com.be.whereu.model.dto;


import com.be.whereu.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String email;
    private String nick;
    private LocalDate birth;
    private String universityEmail;
    private String universityName;
    private String createAt;
    private String modifiedAt;
    private String gender;



    public static MemberDto toDto(MemberEntity memberEntity) {
        return MemberDto.builder()
                .id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .nick(memberEntity.getNick())
                .universityEmail(memberEntity.getUniversityEmail())
                .universityName(memberEntity.getUniversityName())
                .createAt(formattingFromCreateDate(memberEntity))
                .modifiedAt(formattingFromModifiedDate(memberEntity))
                .gender(memberEntity.getGender().name())
                .birth(memberEntity.getBirth())
                .build();

    }
    //git test
    public static String formattingFromCreateDate(MemberEntity memberEntity) {
        LocalDateTime createAt = memberEntity.getCreateAt();
        return createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String formattingFromModifiedDate(MemberEntity memberEntity) {
        LocalDateTime modifiedAt = memberEntity.getModifiedAt();
        return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    }



 }
