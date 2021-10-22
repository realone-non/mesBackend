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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'Lot유형 고유아이디'")
    private Long id;

    @Column(name = "LOT_TYPE", nullable = false, columnDefinition = "bigint COMMENT 'Lot 유형'")
    private String lotType;

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn;
}
