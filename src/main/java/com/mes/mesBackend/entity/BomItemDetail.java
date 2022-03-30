package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/*
 * BOM품목 정보 등록
 * 검색: 품목계정(체크),품목그룹(체크),품목(검색창),하위품목(검색창)
 * 레벨 (1)
 * 품번 (CF00-00001-01)
 * 품명 (EMI FILTER [AF2-E001DB])
 * 제조사 (경일정밀)
 * 제조사품번 (C-AV2-E001DB-2)
 * 단위 (개,그램)                -> Unit
 * 수량 (1,0.1)
 * 구매처 ()
 * 위치 ()
 * 단가 (13000,10)
 * 금액 (단가*수량)
 * 품목계정 (원재료, 부재료)
 * 공정 ()
 * 사용,
 * 비고
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BOM_ITEM_DETAILS")
@Data
@Table(indexes = {
        @Index(name = "bom_master", columnList = "BOM_MASTERS_ID")
})
public class BomItemDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'BOM 품목 정보 등록 고유아이디'")
    private Long id;

    @Column(name = "LEVEL", columnDefinition = "int COMMENT '레벨'", nullable = false)
    private int level;     // 레벨

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    @Column(name = "AMOUNT", columnDefinition = "float COMMENT '수량'", nullable = false)
    private float amount;     // 수량

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "CLIENT", columnDefinition = "bigint COMMENT '구매처'")
//    private Client toBuy;

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '공정'", nullable = false)
    private WorkProcess workProcess;     // 공정

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;        // 비고

    // 다대일 단방향
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BOM_MASTERS_ID", columnDefinition = "bigint COMMENT 'BomMaster'", nullable = false)
    private BomMaster bomMaster;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void addJoin(
            BomMaster bomMaster,
            Item item,
            WorkProcess workProcess
    ) {
        setBomMaster(bomMaster);
        setItem(item);
        setWorkProcess(workProcess);
    }

    public void update(
            Item newItem,
            WorkProcess newWorkProcess,
            BomItemDetail newBomItemDetail
    ) {
        setLevel(newBomItemDetail.level);
        setItem(newItem);
        setAmount(newBomItemDetail.amount);
        setWorkProcess(newWorkProcess);
        setUseYn(newBomItemDetail.useYn);
        setNote(newBomItemDetail.note);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
