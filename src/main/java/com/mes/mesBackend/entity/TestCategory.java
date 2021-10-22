package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 검사 유형
 * 검사유형 (수입검사, 공정검사, 출하검사)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "TEST_CATEGORIES")
@Data
public class TestCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '검사유형 고유아이디'")
    private Long id;

    @Column(name = "CHECK_CATEGORY", nullable = false, unique = true, columnDefinition = "bigint COMMENT '검사유형'")
    private String checkCategory;       // 검사유형

    @Column(name = "TEST_TYPE", nullable = false, columnDefinition = "bigint COMMENT '검사타입'")
    private String testType;

    @Column(name = "USE_YN", columnDefinition = "bigint COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bigint COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부
}
