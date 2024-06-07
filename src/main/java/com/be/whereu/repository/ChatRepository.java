package com.be.whereu.repository;

import com.be.whereu.model.entity.ChatEntity;
import com.be.whereu.model.entity.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity,Long> {

}
