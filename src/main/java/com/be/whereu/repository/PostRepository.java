package com.be.whereu.repository;

import com.be.whereu.model.entity.CommonEntity;
import com.be.whereu.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Long> {


    Page<PostEntity> findAll(Pageable pageable);

    Page<PostEntity> findByCommonOrderByIdDesc(CommonEntity commonEntity, Pageable pageable);






}
