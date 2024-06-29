package com.be.whereu.repository;

import com.be.whereu.model.entity.ComplaintEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> {
    Page<ComplaintEntity> findAllByPostIsNotNull(Pageable pageable);

    Page<ComplaintEntity> findAllByCommentIsNotNull(Pageable pageable);

    //신고 중복 방지
    @Query("SELECT COUNT(c) > 0 FROM ComplaintEntity c WHERE c.complaintBy.id = :memberId AND c.post.id = :postId")
    boolean existsByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);

    @Query("SELECT COUNT(c) > 0 FROM ComplaintEntity c WHERE c.complaintBy.id = :memberId AND c.comment.id = :commentId")
    boolean existsByMemberIdAndCommentId(@Param("memberId") Long memberId, @Param("commentId") Long commentId);
}

