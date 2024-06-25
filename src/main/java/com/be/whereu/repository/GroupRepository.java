package com.be.whereu.repository;

import com.be.whereu.model.dto.GroupDto;
import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity,Long> {
    @Query("SELECT g FROM GroupEntity g JOIN FETCH g.memberGroup mg JOIN FETCH mg.member WHERE g.id = :groupId")
    Optional<GroupEntity> findGroupWithMembers(@Param("groupId") Long groupId);


    void deleteByHostId(Long hostId);

    boolean existsByHostId(Long hostId);

    List<GroupEntity> findByHostId(Long hostId);

}
