package com.be.whereu.repository;

import com.be.whereu.model.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity,Long> {
}
