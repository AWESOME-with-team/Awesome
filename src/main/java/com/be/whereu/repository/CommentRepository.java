package com.be.whereu.repository;

import com.be.whereu.model.entity.CommentEntity;
import com.be.whereu.model.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT c FROM CommentEntity c WHERE c.post.id=:postId")
    List<CommentEntity> findByPostId(@Param("postId")Long postId,  Pageable pageable);

    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.post.id = :postId")
    int countByPostId(@Param("postId") Long postId);


}
