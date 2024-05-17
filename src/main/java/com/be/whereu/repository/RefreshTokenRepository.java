package com.be.whereu.repository;

import com.be.whereu.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {


    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.token = :token")
    public RefreshTokenEntity findByToken(@Param("token") String token);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.email = :email")
    RefreshTokenEntity findByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE  FROM RefreshTokenEntity rt WHERE rt.email = :email")
    public int removeByToken(@Param("email") String email);



}
