package com.be.whereu.model.entity;

import com.be.whereu.model.dto.board.CommentRequestDto;
import com.be.whereu.model.dto.board.CommentResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


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

    @Column(name = "like_count")
    private Integer likeCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id" ,foreignKey = @ForeignKey(name = "comment_parent_fk"))
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> children;

    // getter 메서드를 덮어쓰기 null인 경우는 0으로 대체
    public Integer getLikeCount(){
        return likeCount !=null ? likeCount : 0;
    }

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
