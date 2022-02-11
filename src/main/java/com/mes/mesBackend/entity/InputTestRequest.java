package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.InputTestDivision;
import com.mes.mesBackend.entity.enumeration.InputTestState;
import com.mes.mesBackend.entity.enumeration.InspectionType;
import com.mes.mesBackend.entity.enumeration.TestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static com.mes.mesBackend.entity.enumeration.InputTestDivision.*;
import static com.mes.mesBackend.entity.enumeration.InputTestState.*;
import static com.mes.mesBackend.entity.enumeration.TestType.NO_TEST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

/*
 * 14-1. 부품수입검사
 * 15-1. 외주수입검사의뢰
 * */
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "INPUT_TEST_REQUESTS")
@Data
public class InputTestRequest extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수입검사 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LOT_MASTER", columnDefinition = "bigint COMMENT 'LOT_MASTER'")
    private LotMaster lotMaster;

    @Column(name = "REQUEST_AMOUNT", columnDefinition = "int COMMENT '요청수량'")
    private int requestAmount;                      // 요청수량

    @Enumerated(STRING)
    @Column(name = "INPUT_TEST_STATE", columnDefinition = "varchar(255) COMMENT '수입검사 상태값'")
    private InputTestState inputTestState = SCHEDULE;      // 수입검사의뢰 상태값

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @Enumerated(STRING)
    @Column(name = "INPUT_TEST_DIVISION", columnDefinition = "varchar(255) COMMENT '외주수입검사, 부품수입검사, 제품검사 구분'", nullable = false)
    private InputTestDivision inputTestDivision;

    @Column(name = "TEST_COMPLETION_REQUEST_DATE", columnDefinition = "datetime COMMENT '검사완료요청일'")
    private LocalDate testCompletionRequestDate;

    @Enumerated(STRING)
    @Column(name = "INSPECTION_TYPE", columnDefinition = "varchar(255) COMMENT '검사방법'")
    private InspectionType inspectionType;  // 검사방법: ex) Sampling, 전수

    public void createInputTestRequest(LotMaster lotMaster, InputTestDivision inputTestDivision, LocalDate testCompletionRequestDate, InspectionType inspectionType) {
        setLotMaster(lotMaster);
        setInputTestState(SCHEDULE);
        setInputTestDivision(inputTestDivision);
        setInspectionType(inspectionType);
        if (inputTestDivision.equals(PRODUCT)) {
            setTestCompletionRequestDate(testCompletionRequestDate);
        } else
            setTestCompletionRequestDate(null);
    }

    public void update(InputTestRequest newInputTestRequest, InputTestDivision inputTestDivision) {
        setInspectionType(newInputTestRequest.inspectionType);
        setRequestAmount(newInputTestRequest.requestAmount);
        if (inputTestDivision.equals(PRODUCT)) {
            setTestCompletionRequestDate(newInputTestRequest.testCompletionRequestDate);
        }
    }

    public void delete() {
        setDeleteYn(true);
    }
}
