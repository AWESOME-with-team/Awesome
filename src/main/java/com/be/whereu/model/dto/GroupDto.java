package com.be.whereu.model.dto;

import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private Long hostId;
    private int total;
    private String groupName;
    private String groupLoc;
    private String gender;
    private String isMatch;
    List<MemberDto> members;

    public static GroupDto toDto(GroupEntity entity) {
        return GroupDto.builder()
                .id(entity.getId())
                .hostId(entity.getHostId())
                .total(entity.getTotal())
                .groupName(entity.getGroupName())
                .groupLoc(entity.getGroupLoc())
                .gender(entity.getGender().name())
                .isMatch(entity.getIsmatch().name())
                .build();
    }
    @Override
    public String toString() {
        return "GroupDto{" +
                "id=" + id +
                ", hostId=" + hostId +
                ", total=" + total +
                ", groupName='" + groupName + '\'' +
                ", groupLoc='" + groupLoc + '\'' +
                ", gender='" + gender + '\'' +
                ", isMatch='" + isMatch + '\'' +
                '}';
    }
}