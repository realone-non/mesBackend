package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/*
 * 3-4-1.불량항목 등록
 * 검색: 불량유형
 * 불량항목코드 (0001)
 * 불량항목명 (불량,재작업,수리)
 * 상위항목 (ROOT,1000)
 * 상위항목명 (ROOT,불량)
 * 순번 9
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "BAD_ITEMS")
@Data
public class BadItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '불량항목 등록 고유아이디'")
    private Long id;

    @Column(name = "BAD_ITEM_CODE", nullable = false, unique = true, columnDefinition = "varchar(255) COMMENT '불량항목코드'")
    private String badItemCode;     //  불량항목코드

    @Column(name = "BAD_ITEM_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '불량항목명'")
    private String badItemName;     // 불량항목명

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '순번'")
    private int orders;     // 순번

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WORK_PROCESS", columnDefinition = "bigint COMMENT '작업공정'")
    private WorkProcess workProcess;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Column(name = "EXHAUST_ITEM", columnDefinition = "varchar(255) COMMENT '소진 원부자재'", nullable = false)
    private String exhaustItem;

    public void update(BadItem newBadItem, WorkProcess newWorkProcess) {
        setBadItemCode(newBadItem.badItemCode);
        setBadItemName(newBadItem.badItemName);
        setOrders(newBadItem.orders);
        setUseYn(newBadItem.useYn);
        setWorkProcess(newWorkProcess);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void add(WorkProcess workProcess) {
        setWorkProcess(workProcess);
    }
}
