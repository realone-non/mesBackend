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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부서 고유아이디'")
    private Long id;

    @Column(name = "DEPT_CODE", nullable = false, columnDefinition = "int COMMENT '부서코드'")
    private int deptCode; // 부서코드

    @Column(name = "DEPT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '부서명'")
    private String deptName;    // 부서명

    @Column(name = "SHORT_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '부서약어명'")
    private String shortName;   // 부서약어명

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '순번'")
    private int orders;         // 순번

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void put(Department newDepartment) {
        setDeptCode(newDepartment.deptCode);
        setDeptName(newDepartment.deptName);
        setShortName(newDepartment.shortName);
        setOrders(newDepartment.orders);
        setUseYn(newDepartment.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
