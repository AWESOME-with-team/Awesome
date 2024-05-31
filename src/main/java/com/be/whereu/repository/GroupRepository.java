package com.be.whereu.repository;

import com.be.whereu.model.entity.GroupEntity;
import com.be.whereu.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity,Long> {
    Optional<GroupEntity> findByGroupName(String groupName);

}
