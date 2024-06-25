package com.be.whereu.model.entity;

import com.be.whereu.model.Gender;
import com.be.whereu.model.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birth;
    @Column(name = "u_email")
    private String universityEmail;
    @Column(name= "u_Name")
    private String universityName;



    @OneToMany(mappedBy = "member" ,cascade = CascadeType.ALL, orphanRemoval = true )
    private List<MemberGroupEntity> groupList;
    @Transient
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private SchoolEntity school;
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMemberGroupEntity> chatList;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> postList;
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentList;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLikeEntity> likedPosts ;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLikeEntity> likedComments;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScrapEntity> scrapList;
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH} )
    private List<MessageEntity> messageList; // 추가된 부분
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RefreshTokenEntity refreshToken;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupRequestEntity> groupRequest;

    @PreRemove
    private void preRemove() {

        //member 삭제시 messageEntity의 memberId를 null로 설정
        for (MessageEntity message : messageList) {
            message.setMember(null);
        }

    }



    public static MemberEntity toMemberEntity(MemberDto dto) {
        return MemberEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .nick(dto.getNick())
                .birth(dto.getBirth())
                .universityEmail(dto.getUniversityEmail())
                .universityName(dto.getUniversityName())
                .gender(Gender.fromString(dto.getGender()))
                .build();
    }
}
