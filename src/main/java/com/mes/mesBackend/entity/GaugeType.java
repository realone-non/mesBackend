package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* GAUGE유형
* */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "GAUGE_TYPES")
@Data
public class GaugeType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'GAUGE유형 고유아이디'")
    private Long id;

    @Column(name = "GAUGE_TYPE", columnDefinition = "bigint COMMENT 'GAUGE유형'")
    private String gaugeType;   // GAUGE유형

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bigint COMMENT '사용여부'")
    private boolean useYn = true;  // 사용여부

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bigint COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
