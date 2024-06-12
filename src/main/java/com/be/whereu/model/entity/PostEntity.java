package com.be.whereu.model.entity;

import com.be.whereu.model.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "post")
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_id")
    private CommonEntity common;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "post_member_fk"))
    private MemberEntity member;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;

    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Integer getViewCount(){
        return viewCount !=null ? viewCount : 0;
    }

    public Integer getLikeCount(){
        return likeCount !=null ? likeCount : 0;
    }



    public static PostEntity toPostEntity(PostDto dto){
        MemberEntity member=new MemberEntity();
        CommonEntity common=new CommonEntity();
        member.setNick(dto.getNick());
        common.setCodeId(dto.getCommonId());

        return  PostEntity.builder()
                .id(dto.getId())
                .common(common)
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }


}