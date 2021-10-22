package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 검사타입
* ex) 수동검사, 자동검사
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TEST_TYPES")
@Data
public class TestType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '검사타입 고유아이디'")
    private Long id;

    @Column(name = "TEST_TYPE", nullable = false, columnDefinition = "bigint COMMENT '검사타입'")
    private String testType;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;
}
