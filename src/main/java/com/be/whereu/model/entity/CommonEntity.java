package com.be.whereu.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "common")
public class CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "parent_code_id")
    private Long parentCodeId;

    @Column(name = "code_name",nullable = false)
    private String codeName;

    @Column(name = "code_value")
    private String codeValue;






 }
