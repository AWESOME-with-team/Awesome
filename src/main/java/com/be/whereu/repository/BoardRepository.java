package com.be.whereu.repository;

import com.be.whereu.model.dto.board.BoardListDto;
import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface BoardRepository extends JpaRepository<PostEntity,Long> {

//    //게시판 불러오기 parent
//    @Query("SELECT new com.be.whereu.model.dto.board.BoardListDto(c.codeId, c.codeName, p.title) " +
//            "FROM PostEntity p " +
//            "JOIN p.common c " +
//            "WHERE c.parentCodeId = :parentId " +
//            "ORDER BY p.id DESC")
//    List<BoardListDto> findBoardListDtoByParentId(@Param("parentId") Integer parentId);

    Page<PostEntity> findAll(Pageable pageable);
    Page<PostEntity> findByCommonOrderByIdDesc(CommonEntity commonEntity, Pageable pageable);



}
