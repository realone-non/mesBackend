package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 작업표준서 등록
 * 검색: 공장(드롭), 품목그룹(체크), 품목계정(체크), 품번(텍스트), 품명(텍스트)
 * 작업공정 (조립)
 * 작업라인 (조립라인)
 * 생산품번 (AA01-AF2-U005A)         -> BOM등록 (생산품번)
 * 품명 (EMI FILTER [AF2-U005A])    -> BOM등록 (생산품번)
 * 순번 (1)
 * 파일명 (EQS-AF2U005A-00 작업지도서 20160420.pdf)
 * 비고
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "WORK_DOCUMENTS")
@Data
public class WorkDocument extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne @JoinColumn(name = "WORK_PROCESSES_ID", nullable = false)
    private WorkProcess workProcess;        // 작업공정

    @OneToOne @JoinColumn(name = "WORK_LINES_ID", nullable = false)
    private WorkLine workLine;              // 작업라인

    @OneToOne @JoinColumn(name = "BOM_MASTERS_ID", nullable = false)
    private BomMaster bomMaster;            // 생산품번

    @Column(name = "ORDERS", nullable = false)
    private int orders;                     // 순번

    @Column(name = "FILE_NAME_URL")
    private String fileNameUrl;             // 파일명

    @Column(name = "NOTE")
    private String note;                    // 비고

    @Column(name = "USE_YN")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장
}
