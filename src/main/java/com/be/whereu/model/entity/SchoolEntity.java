package com.be.whereu.model.entity;

import com.be.whereu.model.School;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school_tbl")
public class SchoolEntity {
    @Id
    private Long id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "schoolName", column = @Column(name = "e_school_name")),
            @AttributeOverride(name = "graduateAt", column = @Column(name = "e_graduate_at"))
    })
    private School elementarySchool;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "schoolName", column = @Column(name = "m_school_name")),
            @AttributeOverride(name = "graduateAt", column = @Column(name = "m_graduate_at"))
    })
    private School middleSchool;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "schoolName", column = @Column(name = "h_school_name")),
            @AttributeOverride(name = "graduateAt", column = @Column(name = "h_graduate_at"))
    })
    private School highSchool;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @Setter
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "school_member_fk"))
    private MemberEntity member;



}
