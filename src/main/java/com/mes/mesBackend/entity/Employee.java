package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 직원(작업자)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "EMPLOYEES")
@Data
public class Employee extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '직원 고유아이디'")
    private Long id;

    @Column(name = "EMP_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '사번'")
    private String empCode;     // 사번

    @Column(name = "KOR_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '이름'")
    private String korName;    // 이름

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENTS_ID", nullable = false, columnDefinition = "bigint COMMENT '부서'")
    private Department department;  // 부서

    @Column(name = "POSITION", nullable = false, columnDefinition = "varchar(255) COMMENT '직위'")
    private String position;    // 직위

    @Column(name = "JOP", nullable = false, columnDefinition = "varchar(255) COMMENT '직무'")
    private String jop;         // 직무

    @Column(name = "TEL_NUMBER", columnDefinition = "varchar(255) COMMENT '연락처'")
    private String telNumber;   // 연락처

    @Column(name = "ENG_NAME", columnDefinition = "varchar(255) COMMENT '영문이름+직위'")
    private String engName;     // 영문이름+직위

    @Column(name = "MAIL", nullable = false, columnDefinition = "varchar(255) COMMENT '메일'")
    private String mail;        // 메일

    @Column(name = "PHONE_NUMBER", nullable = false, columnDefinition = "varchar(255) COMMENT '휴대폰번호'")
    private String phoneNumber; // 휴대폰 번호(카카오)

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void put(Employee newEmployee, Department newDepartment) {
        setEmpCode(newEmployee.empCode);
        setKorName(newEmployee.korName);
        setDepartment(newDepartment);
        setPosition(newEmployee.position);
        setJop(newEmployee.jop);
        setTelNumber(newEmployee.telNumber);
        setEngName(newEmployee.engName);
        setMail(newEmployee.mail);
        setPhoneNumber(newEmployee.phoneNumber);
        setUseYn(newEmployee.useYn);
    }
}
