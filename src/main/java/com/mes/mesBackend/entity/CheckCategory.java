package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 검사 유형
 * 검사유형 : 수입검사, 공정검사, 출하검사
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CHECK_CATEGORIES")
@Data
public class CheckCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private Long id;

    @Column(name = "CHECK_CATEGORY", nullable = false, unique = true)
    private String checkCategory;       // 검사유형

    @Column(name = "TEST_TYPE", nullable = false)
    private String testType;

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
