package com.be.whereu.repository;

import com.be.whereu.model.entity.ScrapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<ScrapEntity,Long> {

    public Boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    public void deleteByPostIdAndMemberId(Long postId, Long memberId);


}
