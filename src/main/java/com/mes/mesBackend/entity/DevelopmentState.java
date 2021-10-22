package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 개발등록 진행상태
 * 진행상태 (PLAN-개발의뢰서, Design-제품도면)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DEVELOPMENT_STATES")
@Data
public class DevelopmentState extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '개발등록 진행상태 고유아이디'")
    private Long id;

    @Column(name = "STATE", nullable = false, columnDefinition = "bigint COMMENT '진행상태'")
    private String state;
}
