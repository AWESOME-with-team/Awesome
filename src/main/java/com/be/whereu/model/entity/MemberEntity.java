package com.be.whereu.model.entity;

import com.be.whereu.model.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;


@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "member_tbl",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="nick_unique",
                        columnNames = {"nick"}
                ),
                @UniqueConstraint(
                        name = "email_unique",
                        columnNames = {"email"}
                )
        }
)
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nick;
    private LocalDate birth;
    @Column(name = "u_email")
    private String universityEmail;
    @Column(name= "u_Name")
    private String universityName;
    @Column(name= "u_major")
    private String universityMajor;
    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL, orphanRemoval = true )
    private List<MemberGroupEntity> GroupList;
    @Transient
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private SchoolEntity school;
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMemberEntity> chatList;


    public static MemberEntity toMemberEntity(MemberDto dto) {
        return MemberEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .nick(dto.getNick())
                .birth(dto.getBirth())
                .universityEmail(dto.getUniversityEmail())
                .universityName(dto.getUniversityName())
                .universityMajor(dto.getUniversityMajor())
//                .GroupList(dto.getGroupList())
//                .school(dto.getSchool())
//                .chatList(dto.getChatList())
                .build();
    }
}
