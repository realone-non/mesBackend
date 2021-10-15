package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

// BOM 마스터 정보
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BOM_MASTERS")
@Data
public class BomMaster extends BaseTimeEntity {
    /*
     *  BOM MASTER : 품번, 품명, 품목계정, 제조사 품번, BOM 번호, 유효시작일, 유효종료일, 개발상태, 비고, 승인일시, 사용
     *  BOM ITEM INFO : 레벨, 품번, 품명, 제조사, 제조사 품번, 단위, 수량, 구매처, 위치, 단가, 금약, 품목계정, 공정, 사용, 비고
     *  품목등록  : 생산할 제품
     *  BOM 마스터정보 : 생산할 제품
     *  BOM 품목정보 : 생산한 제품에 대한 재료들
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    /*
     *  품목등록 테이블 참조해서
     *  품번, 품명, 품목계정, 제조사 품번
     */
    @OneToOne @JoinColumn(name = "ITEM_NO")
    private Item itemNo;

    /*
     * BOM등록 테이블에서 관리할 컬럼
     * BOM번호(같은 품번이 같은 번호 생성 X), 유효시작일, 유효종료일, 개발상태, 비고, 승인일시, 사용,
     */

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
