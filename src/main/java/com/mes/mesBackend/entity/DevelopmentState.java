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
@Entity(name = "DEVELOPMENT_STATE")
@Data
public class DevelopmentState extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "STATE", nullable = false)
    private String state;
}
