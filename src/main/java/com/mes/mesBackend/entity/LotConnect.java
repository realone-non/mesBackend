package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.LotConnectDivision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// lotMaster 연관
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "LOT_CONNECTS")
@Data
public class LotConnect extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '로트 연관 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_LOT", columnDefinition = "bigint COMMENT '부모 로트'", nullable = false)
    private LotEquipmentConnect parentLot;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CHILD_LOT", columnDefinition = "bigint COMMENT '자식 로트'", nullable = false)
    private LotMaster childLot;

    @Column(name = "AMOUNT", columnDefinition = "int COMMENT '수량'", nullable = false)
    private int amount;

    @Enumerated(STRING)
    @Column(name = "DIVISION", columnDefinition = "varchar(255) COMMENT '데이터 타입 구분'", nullable = false)
    private LotConnectDivision division;

    @Column(name = "ERROR_YN", columnDefinition = "bit(1) COMMENT '고장 여부'", nullable = false)
    private boolean errorYn;

    public void create(
            LotEquipmentConnect parentLot,
            LotMaster childLot,
            int amount,
            LotConnectDivision division
    ) {
        setParentLot(parentLot);
        setChildLot(childLot);
        setAmount(amount);
        setDivision(division);
    }
}
