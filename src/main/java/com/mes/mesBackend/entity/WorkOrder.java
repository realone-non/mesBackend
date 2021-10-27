package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/*
 * 6-2. 제조오더 등록
 * 검색: 공장,품목그룹,품목,수주번호,제조오더번호,작업공정,착수예정일,지시상태
 * 제조오더번호                                            -> ProduceOrder
 * 품번                                                  -> ProduceOrder
 * 모델명                                                -> ProduceOrder
 * 착수예정일                                             -> ProduceOrder
 * 오더수량                                              -> ProduceOrder
 * 시작 시리얼번호
 * 단위                                                 -> ProduceOrder
 * 지시상태                                               -> ProduceOrder
 * 수주유형                                               -> ProduceOrder
 * 수주처                                                 -> ProduceOrder
 * 수주번호                                               -> ProduceOrder
 * 납기일자                                               -> ProduceOrder
 * 비고
 * 작업지시정보: 지시번호,작업공정,지시수량,생산담당자,단위,준비시간(분),UPH,소요시간(분),작업예정일,예정시간,지시상태,
 * 검사의뢰,검사유형,최종공정,생산수량,투입인원,비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_ORDERS")
@Data
public class WorkOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '제조오더 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCE_ORDER", nullable = false, columnDefinition = "bigint COMMENT '제조오더'")
    private ProduceOrder produceOrder;      // 제조오더

    @Column(name = "START_SERIAL_NO", columnDefinition = "varchar(255) COMMENT '시작 시리얼 번호'")
    private String startSerialNo;           // 시작 시리얼번호

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                    // 비고

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'", nullable = false)
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTORY", columnDefinition = "bigint COMMENT '공장'")
    private Factory factory;                // 공장
}
