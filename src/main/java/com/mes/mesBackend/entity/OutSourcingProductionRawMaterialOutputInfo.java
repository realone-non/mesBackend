package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.OutsourcingMaterialReleaseRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주생산 원재료 출고대상 정보 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;

    @Column(name = "OUTPUT_REQUEST_AMOUNT", columnDefinition = "int COMMENT '출고요청량'")
    private int outputRequestAmount;        // 출고요청량

    @Column(name = "OUTPUT_AMOUNT", columnDefinition = "int COMMENT '출고량'")
    private int outputAmount;               // 출고량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_SOURCING_PRODUCTION_REQUEST", columnDefinition = "bigint COMMENT '외주생산의뢰'")
    private OutSourcingProductionRequest outSourcingProductionRequest;

//    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
//    private boolean useYn = true;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void update(OutSourcingProductionRawMaterialOutputInfo info, Item item){
        setOutputRequestAmount(info.outputRequestAmount);
        setOutputAmount(info.outputAmount);
        setItem(item);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void put(Item item, OutSourcingProductionRequest outSourcingProductionRequest) {
        setItem(item);
        setOutSourcingProductionRequest(outSourcingProductionRequest);
    }
}
