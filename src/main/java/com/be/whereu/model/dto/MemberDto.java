package com.be.whereu.model.dto;


import com.be.whereu.model.Gender;
import com.be.whereu.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;
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
    private String profile;


    public MemberDto(Long id, String email, String nick, LocalDate birth, String universityEmail, String universityName, LocalDateTime createAt, LocalDateTime modifiedAt,  Gender gender, String profile) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.id = id;
        this.email = email;
        this.nick = nick;
        this.birth = birth;
        this.universityEmail = universityEmail;
        this.universityName = universityName;
        this.createAt = createAt.format(formatter);
        this.modifiedAt = modifiedAt.format(formatter);
        this.gender=gender.toString();
        this.profile= profile;
    }


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
                .profile(memberEntity.getProfile())
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
