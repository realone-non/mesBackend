package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 개발등록
 * 검색: 거래처,의뢰기간,기구담당자,진행상태,품목,PCB담당자
 * 거래처코드 (1238157275)                    -> Client
 * 거래처명 (바인텔레콤(주))                       -> Client
 * 품번 (AA01-NC2-A007L)                       -> Item
 * 품명 (EMI FILTER [NC2-A007L])               -> Item
 * 사업명 (VT-21061509)
 * 의뢰일자 (2021.6.23)
 * 납기일자 (2021.9.3)
 * 진행상태 (PLAN-개발의뢰서)
 * 납품수량 (1)
 * 커넥터 사급여부 (예)
 * 기구담당자 (이정환)
 * PCB담당자 (오지원)
 * 수주 파일 (0 Files)               -> 미구현
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "DEVELOPMENTS")
@Data
public class Development extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '개발등록 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", nullable = false, columnDefinition = "bigint COMMENT '품목'")
    private Item item;          // 품목

    @Column(name = "BUSINESS_NAME", columnDefinition = "varchar(255) COMMENT '사업명'")
    private String businessName;        // 사업명

    @Column(name = "REQUEST_DATE", columnDefinition = "date COMMENT '의뢰일자'")
    private LocalDate requestDate;      // 의뢰일자

    @Column(name = "PERIOD_DATE", columnDefinition = "date COMMENT '납기일자'")
    private LocalDate periodDate;       // 납기일자

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVELOPMENT_STATE", columnDefinition = "bigint COMMENT '진행상태'")
    private DevelopmentState developmentState;       // 진행상태

    @Column(name = "DELIVER_AMOUNT", columnDefinition = "int COMMENT '납품수량'")
    private int deliverAmount;          // 납품수량

    @Column(name = "CONNECTOR_YN", columnDefinition = "bit(1) COMMENT '커넥터 사급여부'")
    private boolean connectorYn;     // 커넥터 사급여부

    @Column(name = "MACHINE_MANAGER", columnDefinition = "varchar(255) COMMENT '기구담당자'")
    private String machineManager;  // 기구담당자

    @Column(name = "PCB_MAN0AGER", columnDefinition = "varchar(255) COMMENT 'PCB담당자'")
    private String PcbManager;      // PCB담당자

    @Column(name = "FILE_URL", columnDefinition = "varchar(255) COMMENT '수주파일'")
    private String fileUrl;         // 수주파일

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
