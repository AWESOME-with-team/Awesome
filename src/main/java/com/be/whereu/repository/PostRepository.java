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

public interface PostRepository extends JpaRepository<PostEntity,Long> {


    Page<PostEntity> findAll(Pageable pageable);

    @Query("SELECT new com.be.whereu.model.dto.board.BoardDetailsListDto(p.id, m.nick, p.title, p.content, p.createAt, COUNT(distinct c.id), COUNT(distinct l.id)) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments c " +
            "Left Join p.likedMembers l "+
            "WHERE p.common.codeId = :commonId " +
            "GROUP BY p.id, m.nick, p.title, p.content, p.createAt " +
            "ORDER BY p.id DESC")
    Page<BoardDetailsListDto> findByCommonIdOrderByIdDescWithCommentCount(@Param("commonId") Long commonId, Pageable pageable);


    Page<PostEntity> findByCommonOrderByIdDesc(CommonEntity commonEntity, Pageable pageable);

}







