package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* 검사기준
* 검사기준 (품목확인)
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TEST_CRITERIAS")
@Data
public class TestCriteria extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "TEST_CRITERIA", nullable = false)
    private String testCriteria;

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;
}
