package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.RequestMaterialStockInspect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Entity(name = "MATERIAL_STOCK_INSPECT")
public class MaterialStockInspect extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '재고실사 고유아이디'")
    private Long id;

    @Column(name = "WAREHOUSE", columnDefinition = "nvarchar(255) COMMENT '창고'")
    private String warehouse;

    @Column(name = "ITEM_ACCOUNT", columnDefinition = "nvarchar(255) COMMENT '품목계정'")
    private String itemAccount;

    @Column(name = "ITEM_NO", columnDefinition = "nvarchar(255) COMMENT '품번'")
    private String itemNo;

    @Column(name = "ITEM_NAME", columnDefinition = "nvarchar(255) COMMENT '품명'")
    private String itemName;

    @Column(name = "LOT_TYPE", columnDefinition = "nvarchar(255) COMMENT 'LOT유형'")
    private String lotType;

    @Column(name = "LOT_NO", columnDefinition = "nvarchar(255) COMMENT 'LOT번호'")
    private String lotNo;

    @Column(name = "DB_AMOUNT", columnDefinition = "int COMMENT 'DB수량'")
    private int dbAmount;

    @Column(name = "INSPECT_AMOUNT", columnDefinition = "int COMMENT '실사수량'")
    private int inspectAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '승인자'")
    private User user;

    @Column(name = "APPROVAL_DATE", columnDefinition = "date COMMENT '승인날짜'")
    private LocalDate approvalDate;

    @Column(name = "NOTE", columnDefinition = "nvarchar(255) COMMENT '비고'")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_STOCK_INSPECT_REQUEST", columnDefinition = "bigint COMMENT '재고실사의뢰'", nullable = false)
    private MaterialStockInspectRequest materialStockInspectRequest;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    public void update(RequestMaterialStockInspect request, User user){
        setInspectAmount(request.getInspectAmount());
        setNote(request.getNote());
    }

    public void delete(){
        setDeleteYn(true);
    }
}
