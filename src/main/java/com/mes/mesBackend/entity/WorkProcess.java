package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.WorkProcessDivision;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/*
 * 작업공정 등록
 * 검색조건 : 공장
 * 작업공정코드(01)       -> WorkProcessCode
 * 작업공정명(조립,공정검사,몰딩,출하검사),
 * 공정검사(예,아니오),
 * 공정순번(01,02,03),
 * 사용,
 * 공장(보이진 않고 검색조건)
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "WORK_PROCESSES")
@Data
public class WorkProcess extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '작업공정 고유아이디'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_PROCESS_CODE", columnDefinition = "bigint COMMENT '작업공정코드'")
    private WorkProcessCode workProcessCode;

    @Column(name = "WORK_PROCESS_NAME", nullable = false, columnDefinition = "varchar(255) COMMENT '작업공정명'")
    private String workProcessName;     // 작업공정명

    @Column(name = "PROCESS_TEST", nullable = false, columnDefinition = "bit(1) COMMENT '공정검사'")
    private boolean processTest;        // 공정검사

    @Column(name = "ORDERS", nullable = false, columnDefinition = "int COMMENT '공정순번'")
    private int orders;              // 공정순번

    @Column(name = "USE_YN", columnDefinition = "bit(1) COMMENT '사용여부'")
    private boolean useYn = true;   // 사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부

    @Enumerated(STRING)
    @Column(name = "WORK_PROCESS_DIVISION", columnDefinition = "varchar(255) COMMENT '공정구분'")
    private WorkProcessDivision workProcessDivision;

    @Column(name = "RECYCLE_YN", columnDefinition = "bit(1) COMMENT '재사용 공정 여부'")
    private boolean recycleYn = false;  // 라벨링, 포장

    // 연관 매핑 편리메서드
    public void addWorkProcessCode(WorkProcessCode workProcessCode) {
        setWorkProcessCode(workProcessCode);
    }

    // 수정
    public void put(WorkProcess newWorkProcess, WorkProcessCode newWorkProcessCode) {
        setWorkProcessCode(newWorkProcessCode);
        setWorkProcessName(newWorkProcess.workProcessName);
        setProcessTest(newWorkProcess.processTest);
        setOrders(newWorkProcess.orders);
        setUseYn(newWorkProcess.useYn);
    }

    public void delete() {
        setDeleteYn(true);
    }
}
