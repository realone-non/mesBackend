package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.StockInspectionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 11-1 재고실사 의뢰 정보 등록
 * 검색: 공장, 실사기간
 * 요청번호
 * 실사일자
 * 비고
 * 재고조사 의뢰 상세 정보: 창고유형,창고,품목그룹,품번,품명
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MATERIAL_STOCK_INSPECT_REQUESTS")
@Data
public class MaterialStockInspectRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재고조사 의뢰정보 고유아이디'")
    private Long id;

    @Column(name = "INSPECT_DATE",
            columnDefinition = "date COMMENT '실사일자'")
    private LocalDate inspectDate;      // 실사일자

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WAREHOUSE", columnDefinition = "bigint COMMENT '창고'")
    private WareHouse wareHouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ACCOUNT", columnDefinition = "bigint COMMENT '품목그룹'")
    private ItemAccount itemAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "INSPECTION_TYPE", columnDefinition = "nvarchar(255) COMMENT '실사의뢰타입'")
    private StockInspectionType inspectionType;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;

    public void update(String note, WareHouse wareHouse, ItemAccount itemAccount, StockInspectionType inspectionType){
        setNote(note);
        setWareHouse(wareHouse);
        setItemAccount(itemAccount);
        setInspectionType(inspectionType);
    }

    public void delete(Long id){
        setDeleteYn(true);
    }

}
