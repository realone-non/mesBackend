package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 외주 생산 의뢰 등록
 * 검색: 공장,외주처,생산품목,의뢰기간
 * 외주처                   -> BomMaster
 * 생산품번                  -> BomMaster
 * 생산품명                  -> BomMaster
 * BOM번호                  -> BomMaster
 * 생산요청일자
 * 생산수량
 * 자재출고 요청일자
 * 납기일시
 * 수입검사여부
 * 비고
 * 외주 생산 원재료 출고 대상 정보: 품번,품명,BOM수량,소요량,손실율,출고요청량,출고량
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OUT_SOURCING_PRODUCTION_REQUESTS")
@Data
public class OutSourcingProductionRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주 생산의뢰 등록 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM", columnDefinition = "bigint COMMENT '품목'", nullable = false)
    private Item item;

    @Column(name = "PRODUCTION_DATE", columnDefinition = "date COMMENT '생산요청일자'")
    private LocalDate productionDate;       // 생산요청일자

    @Column(name = "PRODUCTION_AMOUNT", columnDefinition = "int COMMENT '생산수량'")
    private int productionAmount;           // 생산수량

    @Column(name = "MATERIAL_REQUEST_DATE", columnDefinition = "date COMMENT '자제출고일자'")
    private LocalDate materialRequestDate;      // 자재출고일자

    @Column(name = "PERIOD_DATE", columnDefinition = "date COMMENT '납기일시'")
    private LocalDate periodDate;               // 납기일시

    @Column(name = "INPUT_TEST_YN", columnDefinition = "bit(1) COMMENT '수입검사여부'")
    private boolean inputTestYn;                // 수입검사여부

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                        // 비고

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void update(Item item, OutsourcingProductionRequestRequest request){
        setItem(item);
        setProductionAmount(request.getProductionAmount());
        setMaterialRequestDate(request.getMaterialRequestDate());
        setPeriodDate(request.getPeriodDate());
        setInputTestYn(request.isInputTestYn());
        setNote(request.getNote());
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void put(Item item) {
        setItem(item);
        setProductionDate(LocalDate.now());
    }
}
