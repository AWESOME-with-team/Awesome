package com.be.whereu.model.dto;

import com.be.whereu.model.entity.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Long id;
    private String groupName;
    private String groupLoc;
    private String gender;
    private Long hostId;

    public static GroupDto toDto(GroupEntity groupEntity) {
        return GroupDto.builder()
                .id(groupEntity.getId())
                .groupName(groupEntity.getGroupName())
                .groupLoc(groupEntity.getGroupLoc())
                .gender(groupEntity.getGender())
                .hostId(groupEntity.getHostId())
                .build();
    }
}