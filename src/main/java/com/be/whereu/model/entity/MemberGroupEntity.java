package com.be.whereu.model.entity;

import com.be.whereu.model.dto.GroupDto;
import jakarta.persistence.*;
import lombok.*;

import javax.naming.Name;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_group_tbl")
public class MemberGroupEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id" ,foreignKey =  @ForeignKey(name="member_group_fk"))
    private MemberEntity member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "group_id", foreignKey =  @ForeignKey(name="group_member_fk"))
    private GroupEntity group;

    public static MemberGroupEntity ToMemberGroupEntity(GroupEntity groupEntity, Long memberId) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberId);
        MemberGroupEntity memberGroupEntity = new MemberGroupEntity();
        memberGroupEntity.setGroup(groupEntity);
        memberGroupEntity.setMember(memberEntity);

        return memberGroupEntity;
    }
}
