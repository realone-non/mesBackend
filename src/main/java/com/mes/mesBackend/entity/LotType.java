package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Lot 유형
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "LOT_TYPES")
@Data
public class LotType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'Lot유형 고유아이디'")
    private Long id;

    @Column(name = "LOT_TYPE", nullable = false, columnDefinition = "varchar(255) COMMENT 'Lot 유형'")
    private String lotType;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn;

    public void put(LotType newLotType) {
        setLotType(newLotType.lotType);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
