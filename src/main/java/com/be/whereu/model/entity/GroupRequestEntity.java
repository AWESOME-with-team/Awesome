package com.be.whereu.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_reuest_tbl")
public class GroupRequestEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name="group_id_request_fk"))
    private GroupEntity group;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "invite_id" , foreignKey = @ForeignKey(name="member_id_request_fk"))
    private MemberEntity member;
}
