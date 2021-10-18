package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * BOM등록 -> BOM 마스터 정보
 * 검색: 품목계정, 품목그룹, 품목, 하위품목
 * 품번 (AA01-AF2-E001DB)
 * 품명 (EMI FILTER [AF2-E001DB])
 * 품목계정 (제품)
 * 제조사품번 (EP2-A003D, null)
 * BOM 번호 (1,2)
 * 유효시작일 (2020.11.5)
 * 유효종료일 (2999.12.31)
 * 개발상태 (개발완료)
 * 비고
 * 승일일시
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BOM_MASTERS")
@Data
public class BomMaster extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ITEM_NO")
    private Item itemNo;            // 품번,품명,품목계정,제조사품번,개발상태

    @Column(name = "BOM_NO")
    private Long bomNo;             // BOM번호

    @Column(name = "START_DATE")
    private LocalDate startDate;    // 유효시작일

    @Column(name = "END_DATE")
    private LocalDate endDate;      // 유효종료일

    @Column(name = "DEVELOP_STATUS", nullable = false)
    private DevelopStatus developStatus = DevelopStatus.BEFORE;  // 개발상태

    @Column(name = "NOTE")
    private String note;            // 비고

    @Column(name = "APPROVALDATE")
    private LocalDate approvalDate;     // 승인일시

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;
}
