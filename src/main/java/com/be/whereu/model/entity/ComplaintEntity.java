package com.be.whereu.model.entity;

import com.be.whereu.model.dto.ComplaintCommentRequestDto;
import com.be.whereu.model.dto.ComplaintPostRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaint_tbl")
public class ComplaintEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_by", nullable = false)
    private MemberEntity complaintBy; // 신고한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post; // 신고된 게시물

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment; // 신고된 댓글

    @Column(name = "reason", nullable = false)
    private String reason; // 신고 이유


    public static ComplaintEntity toEntity(ComplaintPostRequestDto dto) {

        PostEntity post = new PostEntity();
        post.setId(dto.getPostId());

        return ComplaintEntity.builder()
                .id(dto.getId())
                .post(post)
                .reason(dto.getReason())
                .build();

    }

    public static ComplaintEntity toEntityComment(ComplaintCommentRequestDto dto) {

        CommentEntity comment = new CommentEntity();
        comment.setId(dto.getCommentId());

        return ComplaintEntity.builder()
                .id(dto.getId())
                .comment(comment)
                .reason(dto.getReason())
                .build();
    }
}
