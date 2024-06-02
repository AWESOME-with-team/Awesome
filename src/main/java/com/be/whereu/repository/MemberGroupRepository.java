package com.be.whereu.repository;

import com.be.whereu.model.entity.MemberGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberGroupRepository extends JpaRepository<MemberGroupEntity,Long> {
    @Query("SELECT mg FROM MemberGroupEntity mg JOIN FETCH mg.group WHERE mg.member.id = :memberId")
    Optional<List<MemberGroupEntity>> findListWithGroupByMemberId(@Param("memberId") Long memberId);
}
