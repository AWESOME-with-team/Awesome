package com.be.whereu.repository;

import com.be.whereu.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    @Query("SELECT m FROM MemberEntity m WHERE m.email=:email")
    public MemberEntity findByEmail(String email);

    public MemberEntity findByNick(String nick);

}
