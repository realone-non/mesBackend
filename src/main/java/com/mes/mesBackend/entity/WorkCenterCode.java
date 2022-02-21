//package com.mes.mesBackend.entity;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//// 작업장 코드 등록 TODO: 삭제
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PUBLIC)
//@Entity(name = "WORK_CENTER_CODES")
//@Data
//public class WorkCenterCode {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업장 코드 고유아이디'")
//    private Long id;
//
//    @Column(name = "CODE", columnDefinition = "varchar(255) COMMENT '코드'", nullable = false)
//    private String code;
//
//    @Column(name = "CODE_NAME", columnDefinition = "varchar(255) COMMENT '코드명'", nullable = false)
//    private String codeName;
//
//    @Column(name = "DESCRIPTION", columnDefinition = "varchar(255) COMMENT '설명'")
//    private String description;
//
//    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
//    private boolean useYn;
//}
