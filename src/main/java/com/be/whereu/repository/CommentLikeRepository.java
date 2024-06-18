package com.be.whereu.repository;

import com.be.whereu.model.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity,Long> {
    boolean existsByCommentIdAndMemberId(Long id, Long memberId);
    void deleteByCommentIdAndMemberId(Long id, Long memberId);

    @Query("SELECT COUNT(c) FROM CommentLikeEntity c WHERE c.comment.id= :commentId")
    Integer countLikesByCommentId(@Param("commentId") Long commentId);
}
