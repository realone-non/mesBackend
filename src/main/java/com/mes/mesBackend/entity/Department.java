package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 부서
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DEPARTMENTS")
@Data
public class Department extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "DEPT_CODE", nullable = false)
    private int deptCode; // 부서코드

    @Column(name = "DEPT_NAME", nullable = false)
    private String deptName;    // 부서명

    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;   // 부서약어명

    @Column(name = "ORDERS", nullable = false)
    private int orders;         // 순번

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn = true;      // 사용여부
}
