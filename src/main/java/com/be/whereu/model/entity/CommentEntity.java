package com.be.whereu.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @Column(name= "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id" ,foreignKey = @ForeignKey(name = "comment_parent_fk"))
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> children;


}