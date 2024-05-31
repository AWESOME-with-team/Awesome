package com.be.whereu.model.dto;

import com.be.whereu.model.entity.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static GroupDto toDto(GroupEntity entity) {
        return GroupDto.builder()
                .id(entity.getId())
                .hostId(entity.getHostId())
                .total(entity.getTotal())
                .groupName(entity.getGroupName())
                .groupLoc(entity.getGroupLoc())
                .gender(entity.getGender())
                .isMatch(entity.getIsmatch().name())
                .build();
    }
}