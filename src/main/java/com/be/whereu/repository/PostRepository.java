package com.be.whereu.repository;

import com.be.whereu.model.dto.board.BoardDetailsListDto;
import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.dto.board.ScrapAndSaveListDto;
import com.be.whereu.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity,Long> {




    @Query("SELECT new com.be.whereu.model.dto.board.BoardDetailsListDto(p.id, m.nick, p.title, p.content, p.createAt, COUNT(distinct c.id), COUNT(distinct l.id), p.viewCount," +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, m.profile) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments c " +
            "LEFT JOIN p.likedMembers l " +
            "LEFT JOIN PostLikeEntity pl ON pl.post.id = p.id AND pl.member.id = :memberId " +
            "LEFT JOIN ScrapEntity s ON s.post.id =p.id AND s.member.id = :memberId " +
            "WHERE p.common.codeId = :commonId " +
            "GROUP BY p.id, m.nick, p.title, p.content, p.createAt,p.viewCount, pl.id,s.id, m.profile " +
            "ORDER BY p.id DESC")
    Page<BoardDetailsListDto> findByCommonIdOrderByIdDescWithCommentCount(@Param("commonId") Long commonId,@Param("memberId") Long memberId, Pageable pageable);




    @Query("SELECT new com.be.whereu.model.dto.board.ScrapAndSaveListDto(p.id, p.common.codeName, m.nick, p.title, p.content, p.createAt, COUNT(distinct c.id), COUNT(distinct l.id), p.viewCount," +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments c " +
            "LEFT JOIN p.likedMembers l " +
            "LEFT JOIN PostLikeEntity pl ON pl.post.id = p.id AND pl.member.id = :memberId " +
            "LEFT JOIN ScrapEntity s ON s.post.id =p.id AND s.member.id = :memberId " +
            "WHERE s.member.id = :memberId " +
            "GROUP BY p.id, p.common.codeName, m.nick, p.title, p.content, p.createAt,p.viewCount, pl.id,s.id " +
            "ORDER BY s.id DESC")
    Page<ScrapAndSaveListDto> findScrapListByMemberId(@Param("memberId") Long memberId, Pageable pageable);




    @Query("SELECT new com.be.whereu.model.dto.board.ScrapAndSaveListDto(p.id, p.common.codeName, m.nick, p.title, p.content, p.createAt, COUNT(distinct c.id), COUNT(distinct l.id), p.viewCount," +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments c " +
            "LEFT JOIN p.likedMembers l " +
            "LEFT JOIN PostLikeEntity pl ON pl.post.id = p.id AND pl.member.id = :memberId " +
            "LEFT JOIN ScrapEntity s ON s.post.id =p.id AND s.member.id = :memberId " +
            "WHERE p.member.id = :memberId " +
            "GROUP BY p.id, p.common.codeName, m.nick, p.title, p.content, p.createAt,p.viewCount, pl.id, s.id " +
            "ORDER BY p.id DESC")
    Page<ScrapAndSaveListDto>findMySavePostByMemberId(@Param("memberId") Long memberId, Pageable pageable);



    @Query("SELECT new com.be.whereu.model.dto.board.BoardListDto(c.codeId, c.codeName, p.title) " +
            "FROM CommonEntity c " +
            "LEFT JOIN PostEntity p ON p.common.codeId = c.codeId " +
            "AND p.id IN (" +
            "    SELECT MAX(p2.id) " +
            "    FROM PostEntity p2 " +
            "    WHERE p2.common.parentCodeId = 1000 " +
            "    GROUP BY p2.common.codeId " +
            ")" +
            "WHERE c.parentCodeId = 1000 " +
            "ORDER BY c.codeId")
    Page<BoardListDto> findByParentIdByWithLastPostTitle(Pageable pageable);

}







