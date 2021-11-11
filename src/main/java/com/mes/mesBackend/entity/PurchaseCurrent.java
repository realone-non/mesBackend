package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.PurchaseState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
/*
 * 9-4. 구매현황 등록
 * 검색: 공장,거래처,품목,입고기간
 * 입고일시 (2021.7.21)
 * 거래처 (1288624828)                          -> Client
 * 거래처명 ((주)케이투코리아)                      -> Client
 * 발주번호 (20210629-003)                       -> PurchaseOrder
 * 품번 (JC00-D24-0008-02)                     -> ProduceOrder
 * 품명 (Connector-Circular)                   -> ProduceOrder
 * 규격 (D38999-24FE6PN(6P))                   -> ProduceOrder
 * 제조사품번 (D38999-24FE6PN(AERO))             -> Client
 * 입고수량 (20)
 * 입고금액 (900000)
 * 입고번호 (3995)
 * 발주단위 (개)
 * 부가세 (90000)
 * LOT 번호 (IA9S5000000001)
 * 구매처 LOT 번호 ()
 * 구매관리 타입
 *    ㄴ> 구매요청, 발주, 구매완료, 입고완료, 반품등록, 반품완료
 * 9-1. 구매요청등록: 선택,제조오더번호,수주처,품번,품명,규격,제조사품번,구매단위,요청일자,요청수량,발주수량,납기일자,수입검사유형,제조사,구매처,구매처명,비고,모델품번,수주납기일
 *    ㄴ> ProduceOrder 참조
 * 9-2. 구매발주등록: 발주번호,발주일자,거래처,거래처명,담당자,입고창고,화폐,환율,부가세적용,납기일자,수주정보,지기상태,비고,운송조건,별첨,운송유형,지불조건,Requested Shipplng(w),특이사항
 *               세부: 요청번호,품번,품명,규격,제조사품번,발주단위,발주수량,단가,발주금액,발구금액(원화),VAT,발주가능수량,입고수량,취소수량,납기일자.비고,수입검사유형,제조사 (여러개)
 **/
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "PURCHASE_CURRENTS")
@Data
public class PurchaseCurrent extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '구매현황 고유아이디'")
    private Long id;            // 구매현황 고유아이디

    @Column(name = "INPUT_DATE", columnDefinition = "date COMMENT '입고일시'")
    private LocalDate inputDate;        // 입고일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '거래처'")
    private Client client;              // 거래처

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCE_ORDER", columnDefinition = "bigint COMMENT '제조오더'")
    private ProduceOrder produceOrder;          // 제조오더 정보 (구매요청에서 필요한 참조)

    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '입고수량'")
    private int inputAmount;                    // 입고수량

    @Column(name = "INPUT_PRICE", columnDefinition = "int COMMENT '입고금액'")
    private int inputPrice;                     // 입고금액

    @Column(name = "INPUT_NO", columnDefinition = "bigint COMMENT '입고번호'")
    private Long inputNo;                       // 입고번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT", columnDefinition = "bigint COMMENT '단위'")
    private Unit unit;                          // 발주단위

    @Column(name = "SURTAX", columnDefinition = "int COMMENT '부가세'")
    private int surtax;                         // 부가세

    @Column(name = "LOT_NO", columnDefinition = "varchar(255) COMMENT 'LOT 번호'")
    private String lotNo;                       // LOT 번호           -> 미구현

    @Column(name = "TO_BUY_LOT_NO", columnDefinition = "varchar(255) COMMENT '구매처 LOT 번호'")
    private String toBuyLotNo;                  // 구매처 LOT 번호      -> 미구현

    @Enumerated(EnumType.STRING)
    @Column(name = "PURCHASE_TYPE", columnDefinition = "varchar(255) COMMENT '구매정보 타입'")
    private PurchaseState purchaseType = PurchaseState.PURCHASE_REQUEST;      // 구매정보 타입

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ORDER", columnDefinition = "bigint COMMENT '구매발주'")
    private PurchaseOrder purchaseOrder;            // 구매발주 정보
}
