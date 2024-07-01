package com.be.whereu.repository;

import com.be.whereu.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    @Query("SELECT m FROM MemberEntity m WHERE m.email=:email")
    MemberEntity findByEmail(String email);

    Optional<MemberEntity> findByNick(String nick);

    @Query("SELECT m FROM MemberEntity m " +
            "LEFT JOIN FETCH m.groupList mg " +
            "WHERE m.nick LIKE %:nick% " +
            "AND m.gender = (SELECT g.gender FROM GroupEntity g WHERE g.id = :groupId) " +
            "AND m.id NOT IN (" +
            "    SELECT mg.member.id FROM MemberGroupEntity mg " +
            "    WHERE mg.group.id = :groupId" +
            ") " +
            "AND m.id NOT IN (" +
            "    SELECT gr.member.id FROM GroupRequestEntity gr " +
            "    WHERE gr.group.id = :groupId" +
            ")")
    Optional<List<MemberEntity>> findMemberListExcludingGroupMemberAndAnotherGenderAndAlreadyRequestedList(@Param("nick") String nick, @Param("groupId") Long groupId);
}
