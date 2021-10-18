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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @Column(name = "WORK_PROCESS_CODE", unique = true, nullable = false)
    private String workProcessCode;     // 작업공정코드

    @Column(name = "WORK_PROCESS_NAME", unique = true, nullable = false)
    private String workProcessName;     // 작업공정명

    @Column(name = "PROCESS_TEST", nullable = false)
    private boolean processTest;        // 공정검사

    @Column(name = "ORDERS", nullable = false, unique = true)
    private String orders;              // 공정순번

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장
}
