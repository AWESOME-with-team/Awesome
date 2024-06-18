package com.be.whereu.repository;

import com.be.whereu.model.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);

    @Query("SELECT COUNT(l) FROM PostLikeEntity l WHERE l.post.id = :postId")
    Integer countLikesByPostId(@Param("postId") Long postId);
}
