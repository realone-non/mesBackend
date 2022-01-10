package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 17-2. 설비 고장 수리 내역 수리부품
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "REPAIR_PARTS")
@Data
public class RepairPart extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수리부품 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "REPAIR_ITEM", columnDefinition = "bigint COMMENT '수리항목'")
    private RepairItem repairItem;

    @Column(name = "REPAIR_PART", columnDefinition = "varchar(255) COMMENT '수리부품'", nullable = false)
    private String repairPart;

    @Column(name = "REPAIR_PART_NAME", columnDefinition = "varchar(255) COMMENT '수리부품명'", nullable = false)
    private String repairPartName;

    @Column(name = "AMOUNT", columnDefinition = "int COMMENT '수량'", nullable = false)
    private int amount;

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;
}
