package com.be.whereu.repository;

import com.be.whereu.model.dto.board.CommentListResponseDto;
import com.be.whereu.model.entity.CommentEntity;
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


    @Query("SELECT new com.be.whereu.model.dto.board.CommentListResponseDto(" +
            "c.id, c.post.id, " +
            "m.id, m.nick,  m.universityName, " +
            "c.content, COUNT(cl.id), c.parent.id, c.createAt,c.modifiedAt) " +
            "FROM CommentEntity c " +
            "LEFT JOIN c.member m " +
            "LEFT JOIN c.likeComments cl " +
            "WHERE c.post.id = :postId " +
            "GROUP BY c.id, m.id, m.email, m.nick, m.birth, m.universityEmail, m.universityName, m.createAt, m.modifiedAt, m.gender, c.content, c.parent.id, c.createAt " +
            "ORDER BY c.id DESC")
    List<CommentListResponseDto> findByPostIdWithLikeCount(@Param("postId") Long postId, Pageable pageable);




}
