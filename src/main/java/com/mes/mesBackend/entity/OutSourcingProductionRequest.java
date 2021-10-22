package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 외주 생산 의뢰 등록
 * 검색: 공장,외주처,생산품목,의뢰기간
 * 외주처                   -> BomMaster
 * 생산품번                  -> BomMaster
 * 생산품명                  -> BomMaster
 * BOM번호                  -> BomMaster
 * 생산요청일자
 * 생산수량
 * 자재출고 요청일자
 * 납기일시
 * 수입검사여부
 * 비고
 * 외주 생산 원재료 출고 대상 정보: 품번,품명,BOM수량,소요량,손실율,출고요청량,출고량
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OUT_SOURCING_PRODUCTION_REQUESTS")
@Data
public class OutSourcingProductionRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOM_MASTER", nullable = false)
    private BomMaster bomMaster;        // BOM

    @Column(name = "PRODUCTION_DATE")
    private LocalDate productionDate;       // 생산요청일자

    @Column(name = "PRODUCTION_AMOUNT")
    private int productionAmount;           // 생산수량

    @Column(name = "MATERIAL_REQUEST_DATE")
    private LocalDate materialRequestDate;      // 자재출고일자

    @Column(name = "PERIOD_DATE")
    private LocalDate periodDate;               // 납기일시

    @Column(name = "INPUT_TEST_YN")
    private boolean inputTestYn;                // 수입검사여부

    @Column(name = "NOTE")
    private String note;                        // 비고

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "FACTORIES_ID")
    private Factory factory;                // 공장

    @Column(name = "USE_YN", nullable = false)
    private boolean useYn;

    @Column(name = "DELETE_YN")
    private boolean deleteYn = false;  // 삭제여부
}
