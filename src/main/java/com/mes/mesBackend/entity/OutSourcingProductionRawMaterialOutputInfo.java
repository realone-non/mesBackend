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
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "BOM_MASTER")
    private BomMaster bomMaster;        // BOM

    @Column(name = "USE_AMOUNT")
    private int useAmount;              // 소요량

    @Column(name = "LOSS_AMOUNT")
    private int lossAmount;            // 손실율

    @Column(name = "OUTPUT_REQUEST_AMOUNT")
    private int outputRequestAmount;        // 출고요청량

    @Column(name = "OUTPUT_AMOUNT")
    private int outputAmount;               // 출고량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_SOURCING_PRODUCTION_REQUEST")
    private OutSourcingProductionRequest outSourcingProductionRequest;
}
