package com.be.whereu.model.entity;


import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.isMatch;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_tbl")
public class GroupEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;
    @Column(name = "host_id")
    private Long hostId;
    private int total;
    @Column(name = "g_name")
    private String groupName;
    @Column(name = "g_loc")
    private String groupLoc;
    private String gender;
    @Enumerated(EnumType.STRING)
    private isMatch isMatch;
    @OneToMany(mappedBy = "group")
    private List<MemberGroupEntity> memberGroup;

    public static GroupEntity toGroupEntity(GroupDto dto){
        return GroupEntity.builder()
                .id(dto.getId())
                .hostId(dto.getHostId())
                .groupName(dto.getGroupName())
                .groupLoc(dto.getGroupLoc())
                .gender(dto.getGender())
                .build();
    }
}
