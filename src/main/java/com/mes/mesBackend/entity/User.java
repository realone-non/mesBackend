package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 직원(작업자)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
@Data
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '직원 고유아이디'")
    private Long id;

    @Column(name = "USER_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '사번'")
    private String userCode;     // 사번

    @Column(name = "PASSWORD", nullable = false, columnDefinition = "varchar(255) COMMENT '비밀번호'")
    private String password;        // 비번

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

    @Column(name = "LEVEL", columnDefinition = "int COMMENT '유저권한레벨'")
    private int level;      // 유저 권한 레벨

    @Column(name = "DESCRIPTION", columnDefinition = "varchar(255) COMMENT '메모'")
    private String description;

    public void put(User newUser, Department newDepartment) {
        setUserCode(newUser.userCode);
        setKorName(newUser.korName);
        setDepartment(newDepartment);
        setPosition(newUser.position);
        setJop(newUser.jop);
        setTelNumber(newUser.telNumber);
        setEngName(newUser.engName);
        setMail(newUser.mail);
        setPhoneNumber(newUser.phoneNumber);
        setUseYn(newUser.useYn);
    }

    public void addJoin(Department department) {
        setDepartment(department);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
