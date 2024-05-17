package com.be.whereu.repository;

import com.be.whereu.model.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolEntity,Long> {
}
