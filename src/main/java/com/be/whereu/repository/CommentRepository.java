package com.be.whereu.repository;

import com.be.whereu.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT c FROM CommentEntity c WHERE c.post.id=:postId")
    List<CommentEntity> findByPostId(@Param("postId")Long postId);

}
