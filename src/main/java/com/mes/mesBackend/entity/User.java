package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

// 직원(작업자)
@AllArgsConstructor
//@NoArgsConstructor
@Entity(name = "USERS")
@Data
public class User extends BaseTimeEntity implements UserDetails {
    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '직원 고유아이디'")
    private Long id;

    @Column(name = "USER_CODE", nullable = false, columnDefinition = "varchar(255) COMMENT '사번'")
    private String userCode;     // 사번

    @Column(name = "PASSWORD", nullable = false, columnDefinition = "varchar(500) COMMENT '비밀번호'")
    private String password;        // 비번

    @Column(name = "SALT", nullable = false, columnDefinition = "varchar(500) COMMENT '솔트'")
    private String salt;

    @Column(name = "KOR_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '이름'")
    private String korName;    // 이름

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "DEPARTMENTS_ID", columnDefinition = "bigint COMMENT '부서'")
    private Department department;  // 부서

    @Column(name = "POSITION",  columnDefinition = "varchar(255) COMMENT '직위'")
    private String position;    // 직위

    @Column(name = "JOb",  columnDefinition = "varchar(255) COMMENT '직무'")
    private String job;         // 직무

    @Column(name = "TEL_NUMBER", columnDefinition = "varchar(255) COMMENT '연락처'")
    private String telNumber;   // 연락처

    @Column(name = "ENG_NAME", columnDefinition = "varchar(255) COMMENT '영문이름'")
    private String engName;     // 영문이름+직위

    @Column(name = "MAIL", nullable = false, columnDefinition = "varchar(255) COMMENT '메일'")
    private String mail;        // 메일

    @Column(name = "PHONE_NUMBER", columnDefinition = "varchar(255) COMMENT '휴대폰번호'")
    private String phoneNumber; // 휴대폰 번호(카카오)

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;  // 사용

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "LEVEL", columnDefinition = "int COMMENT '유저권한레벨'")
    private int level;      // 유저 권한 레벨

    @Column(name = "DESCRIPTION", columnDefinition = "varchar(255) COMMENT '메모'")
    private String description;

    @Column(name = "IMAGE_URL", columnDefinition = "varchar(255) COMMENT '프로필사진 url'")
    private String imageUrl;

    @Column(name = "USER_TYPE", columnDefinition = "varchar(255) COMMENT '권한'", nullable = false)
    @Enumerated(STRING)
    private UserType userType;

    public void put(User newUser, Department newDepartment) {
        setKorName(newUser.korName);
        setDepartment(newDepartment);
        setPosition(newUser.position);
        setJob(newUser.job);
        setTelNumber(newUser.telNumber);
        setEngName(newUser.engName);
        setMail(newUser.mail);
        setPhoneNumber(newUser.phoneNumber);
        setUseYn(newUser.useYn);
        setDescription(newUser.description);
        setLevel(newUser.level);
        setUserType(newUser.userType);
    }

    public void addJoin(Department department) {
        setDepartment(department);
    }

    public void delete() {
        setDeleteYn(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<UserType> roles = new ArrayList<>();
        roles.add(this.userType);
        return roles;
    }

    @Override
    public String getUsername() {
        return userCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
    }
}
