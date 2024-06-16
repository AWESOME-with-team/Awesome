package com.be.whereu.repository;

import com.be.whereu.model.entity.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonRepository extends JpaRepository<CommonEntity,Long> {

    public CommonEntity findByParentCodeId(Integer parentCodeId);
}

