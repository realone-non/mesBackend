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
@Entity(name = "TEST_CRITERIA")
@Data
public class TestCriteria extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '검사기준 고유아이디'")
    private Long id;

    @Column(name = "TEST_CRITERIA", nullable = false, columnDefinition = "varchar(255) COMMENT '검사기준'")
    private String testCriteria;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;


    public void delete() {
        setDeleteYn(true);
    }
    public void put(TestCriteria newTestCriteria) {
        setTestCriteria(newTestCriteria.testCriteria);
    }
}
