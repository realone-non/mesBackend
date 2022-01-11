package com.mes.mesBackend.entity;

import com.mes.mesBackend.dto.request.OutsourcingInputRequest;
import com.mes.mesBackend.dto.request.OutsourcingProductionRequestRequest;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 외주입고 등록
 * 검색: 공장,외주처,LOT입고단위수량,품목,입고기간
 * 외주처
 * 생산요청번호
 * 생산품번
 * 생산품명
 * 입고일시
 * 미입고수량
 * 입고수량
 * 입고창고
 * 검사의뢰유형
 * 비고
 * 외주 입고 LOT정보 -> 미구현
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "OUT_SOURCING_INPUT")
@Data
public class OutSourcingInput extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '외주입고 등록 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_SOURCING_PRODUCTION_REQUEST", columnDefinition = "varchar(255) COMMENT '외주생산요청'")
    private OutSourcingProductionRequest productionRequest;         // 외주처, 생산요청번호, 생산품번, 생산품명

    @Column(name = "INPUT_DATE", nullable = false, columnDefinition = "date COMMENT '입고일시'")
    private LocalDate inputDate;                // 입고일시

    @Column(name = "NO_INPUT_AMOUNT", columnDefinition = "int COMMENT '미입고수량'")
    private int noInputAmount;                  // 미입고수량

    @Column(name = "INPUT_AMOUNT", columnDefinition = "int COMMENT '입고수량'")
    private int inputAmount;                    // 입고수량

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INPUT_WARE_HOUSE", columnDefinition = "bigint COMMENT '입고창고'")
    private WareHouse inputWareHouse;           // 입고창고

    // enum으로 대체
    @Enumerated(EnumType.STRING)
    @Column(name = "TEST_REQUEST_TYPE", columnDefinition = "varchar(255) COMMENT '검사의뢰유향'")
    private TestType testRequestType = TestType.NO_TEST;                  // 검사의뢰유형

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;                        // 비고

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;

    @Column(name = "DELETE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    public void update(OutsourcingInputRequest request, OutSourcingProductionRequest prodRequest, WareHouse wareHouse){
        setInputWareHouse(wareHouse);
        setProductionRequest(prodRequest);
        setInputDate(request.getInputDate());
        setInputAmount(request.getInputAmount());
        setNote(request.getNote());
        setNoInputAmount(request.getNoInputAmount());
    }

    public void delete(){
        setDeleteYn(true);
    }
}
