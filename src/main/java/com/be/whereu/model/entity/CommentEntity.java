package com.be.whereu.model.entity;

import com.be.whereu.model.dto.board.CommentRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false , foreignKey = @ForeignKey(name = "comment_post_fk"))
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "comment_member_fk"))
    private MemberEntity member;

    @Lob
    @Column(nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id" ,foreignKey = @ForeignKey(name = "comment_parent_fk"))
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> children;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLikeEntity> likeComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ComplaintEntity> complaints;

    public static CommentEntity toEntity(CommentRequestDto CommentRequestDto) {
        if (CommentRequestDto == null) {
            return null;
        }

        CommentEntity commentEntity = CommentEntity.builder()
                .id(CommentRequestDto.getId())
                .content(CommentRequestDto.getContent())
                .build();


        // ParentId => ParentEntity에 담아서 저장
        if (CommentRequestDto.getParentId() != null) {
            commentEntity.setParent(CommentEntity.builder().id(CommentRequestDto.getParentId()).build());
        }

        // PostId => PostEntity에 담아서 저장
        if (CommentRequestDto.getPostId() != null) {
            commentEntity.setPost(PostEntity.builder().id(CommentRequestDto.getPostId()).build());
        }


        return commentEntity;
    }
}
