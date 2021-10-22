package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 외주 생산 원재료 출고 대상 정보
 * 품번                       -> BomMaster
 * 품명                        -> BomMaster
 * BOM수량                    -> BomMaster
 * 소요량
 * 손실율
 * 출고요청량
 * 출고량
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OUT_SOURCING_PRODUCTION_MATERIAL_OUTPUT_INFO")
@Data
public class OutSourcingProductionRawMaterialOutputInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주생산 원재료 출고대상 정보 고유아이디'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOM_MASTER", columnDefinition = "bigint COMMENT 'BomMaster'")
    private BomMaster bomMaster;        // BOM

    @Column(name = "USE_AMOUNT", columnDefinition = "bigint COMMENT '소요량'")
    private int useAmount;              // 소요량

    @Column(name = "LOSS_AMOUNT", columnDefinition = "bigint COMMENT '손실율'")
    private int lossAmount;            // 손실율

    @Column(name = "OUTPUT_REQUEST_AMOUNT", columnDefinition = "bigint COMMENT '출고요청량'")
    private int outputRequestAmount;        // 출고요청량

    @Column(name = "OUTPUT_AMOUNT", columnDefinition = "bigint COMMENT '출고량'")
    private int outputAmount;               // 출고량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_SOURCING_PRODUCTION_REQUEST", columnDefinition = "bigint COMMENT '외주생산의뢰'")
    private OutSourcingProductionRequest outSourcingProductionRequest;
}
