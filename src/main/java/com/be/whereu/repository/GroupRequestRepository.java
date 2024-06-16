package com.be.whereu.repository;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.GroupRequestEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRequestRepository extends JpaRepository<GroupRequestEntity,Long> {

    @Query("SELECT g FROM GroupRequestEntity g JOIN FETCH g.member JOIN FETCH g.group where g.member.id = :memberId")
    Optional<List<GroupRequestEntity>> requestGroupList(Long memberId);

    @Modifying
    @Query("DELETE FROM GroupRequestEntity g WHERE g.group.id = :groupId AND g.member.id = :memberId")
    void deleteByGroupRequestIdAndMemberId(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    boolean existsByGroupIdAndMemberId(Long groupId, Long memberId);
}
