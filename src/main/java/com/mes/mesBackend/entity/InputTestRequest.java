package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.InputTestState;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.mes.mesBackend.entity.enumeration.InputTestState.SCHEDULE;
import static com.mes.mesBackend.entity.enumeration.TestType.NO_TEST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 14-1. 부품수입검사
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "INPUT_TEST_REQUESTS")
@Data
public class InputTestRequest extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부품수입검사 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'LOT_MASTER'")
    private LotMaster lotMaster;

    @Enumerated(STRING)
    @Column(name = "REQUEST_TYPE", columnDefinition = "varchar(255) COMMENT '요청유형'")
    private TestType requestType = NO_TEST;    // 요청유형

    @Column(name = "REQUEST_AMOUNT", columnDefinition = "int COMMENT '요청수량'")
    private int requestAmount;                      // 요청수량

    @Column(name = "TEST_AMOUNT", columnDefinition = "int COMMENT '검사수량'")
    private int testAmount;     // 검사수량

    @Enumerated(STRING)
    @Column(name = "INPUT_TEST_STATE", columnDefinition = "varchar(255) COMMENT '부품수입검사 상태값'")
    private InputTestState inputTestState = SCHEDULE;      // 수입검사의뢰 상태값

    @Enumerated(STRING)
    @Column(name = "TEST_TYPE", columnDefinition = "varchar(255) COMMENT '검사유형'")
    private TestType testType = NO_TEST;   // 검사유형

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    public void create(LotMaster lotMaster) {
        setLotMaster(lotMaster);
        setInputTestState(SCHEDULE);
    }

    public void update(LotMaster newLotMaster, InputTestRequest newInputTestRequest) {
        setLotMaster(newLotMaster);
        setRequestType(newInputTestRequest.requestType);
        setRequestAmount(newInputTestRequest.requestAmount);
        setTestType(newInputTestRequest.testType);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
