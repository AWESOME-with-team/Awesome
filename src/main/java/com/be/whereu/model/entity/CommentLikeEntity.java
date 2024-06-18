package com.be.whereu.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_likes")
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "commentlike_comment_fk"))
    private CommentEntity comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "commentlike_member_fk"))
    private MemberEntity member;


    public static CommentLikeEntity toEntity(CommentEntity comment, MemberEntity member) {
        return CommentLikeEntity.builder()
                .comment(comment)
                .member(member)
                .build();
    }
}
