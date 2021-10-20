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
public class Development {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "CLIENT", nullable = false)
    private Client client;      // 거래처

    @OneToOne @JoinColumn(name = "ITEM", nullable = false)
    private Item item;          // 품목

    @Column(name = "BUSINESS_NAME")
    private String businessName;        // 사업명

    @Column(name = "REQUEST_DATE")
    private LocalDate requestDate;      // 의뢰일자

    @Column(name = "PERIOD_DATE")
    private LocalDate periodDate;       // 납기일자

    @Column(name = "PROGRESS_STATE")
    private String progressState;       // 진행상태

    @Column(name = "DELIVER_AMOUNT")
    private int deliverAmount;          // 납품수량

    @Column(name = "CONNECTOR_YN")
    private boolean connectorYn;     // 커넥터 사급여부

    @Column(name = "MACHINE_MANAGER")
    private String machineManager;  // 기구담당자

    @Column(name = "PCB_MAN0AGER")
    private String PcbManager;      // PCB담당자

    @Column(name = "FILE_URL")
    private String fileUrl;         // 수주파일
}
