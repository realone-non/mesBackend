package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 작업공정 등록
 * 검색조건 : 공장
 * 작업공정코드(01),
 * 작업공정명(조립,공정검사,몰딩,출하검사),
 * 공정검사(예,아니오),
 * 공정순번(01,02,03),
 * 사용,
 * 공장(보이진 않고 검색조건)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_PROCESSES")
@Data
public class WorkProcess extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업공정 고유아이디'")
    private Long id;

    @Column(name = "WORK_PROCESS_CODE", unique = true, nullable = false, columnDefinition = "varchar(255) COMMENT '작업공정 코드'")
    private String workProcessCode;     // 작업공정코드

    @Column(name = "WORK_PROCESS_NAME", unique = true, nullable = false, columnDefinition = "varchar(255) COMMENT '작업공정명'")
    private String workProcessName;     // 작업공정명

    @Column(name = "PROCESS_TEST", nullable = false, columnDefinition = "bit(1) COMMENT '공정검사'")
    private boolean processTest;        // 공정검사

    @Column(name = "ORDERS", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '공정순번'")
    private String orders;              // 공정순번

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
