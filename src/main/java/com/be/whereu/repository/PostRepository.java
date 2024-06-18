package com.be.whereu.repository;

import com.be.whereu.model.dto.board.BoardDetailsListDto;
import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity,Long> {

    @Query(value = "SELECT p.id AS postId, m.nick AS nick, p.title AS title, p.content AS content, p.create_at AS createAt, " +
            "COUNT(DISTINCT c.id) AS commentCount, COUNT(DISTINCT l.id) AS likeCount, " +
            "CASE WHEN EXISTS (SELECT 1 FROM post_likes pl WHERE pl.post_id = p.id AND pl.member_id = :memberId) THEN TRUE ELSE FALSE END AS isLiked " +
            "FROM post p " +
            "LEFT JOIN member_tbl m ON p.member_id = m.id " +
            "LEFT JOIN comment c ON c.post_id = p.id " +
            "LEFT JOIN post_likes l ON l.post_id = p.id " +
            "WHERE p.common_id = :commonId " +
            "GROUP BY p.id, m.nick, p.title, p.content, p.create_at " +
            "ORDER BY p.id DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM post p " +
                    "WHERE p.common_id = :commonId",
            nativeQuery = true)
    Page<BoardDetailsListDto> findByCommonIdOrderByIdDescWithCommentCount2(@Param("commonId") Long commonId, @Param("memberId") Long memberId, Pageable pageable);




    Page<PostEntity> findAll(Pageable pageable);

    @Query("SELECT new com.be.whereu.model.dto.board.BoardDetailsListDto(p.id, m.nick, p.title, p.content, p.createAt, COUNT(distinct c.id), COUNT(distinct l.id), " +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments c " +
            "LEFT JOIN p.likedMembers l " +
            "LEFT JOIN PostLikeEntity pl ON pl.post.id = p.id AND pl.member.id = :memberId " +
            "WHERE p.common.codeId = :commonId " +
            "GROUP BY p.id, m.nick, p.title, p.content, p.createAt, pl.id " +
            "ORDER BY p.id DESC")
    Page<BoardDetailsListDto> findByCommonIdOrderByIdDescWithCommentCount(@Param("commonId") Long commonId,@Param("memberId") Long memberId, Pageable pageable);




    Page<PostEntity> findByCommonOrderByIdDesc(CommonEntity commonEntity, Pageable pageable);

    @Query("SELECT new com.be.whereu.model.dto.board.BoardListDto(c.codeId, c.codeName, p.title) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.common c " +
            "WHERE c.parentCodeId = 1000 " +
            "AND p.id IN (" +
            "    SELECT MAX(p2.id) " +
            "    FROM PostEntity p2 " +
            "    WHERE p2.common.parentCodeId = 1000 " +
            "    GROUP BY p2.common.codeId " +
            ")" +
            "ORDER BY p.common.codeId")
    List<BoardListDto> findByParentIdByWithLastPostTitle(Pageable pageable);
}







