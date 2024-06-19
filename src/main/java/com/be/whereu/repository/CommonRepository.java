package com.be.whereu.repository;

import com.be.whereu.model.entity.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonRepository extends JpaRepository<CommonEntity,Long> {
    List<CommonEntity> findByParentCodeId(Long parentId);
}

