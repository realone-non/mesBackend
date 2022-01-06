package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

// 14-2. 검사정보
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "INPUT_TEST_DETAILS")
@Data
public class InputTestDetail extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부품수입검사상세 고유아이디'")
    private Long id;

    // 검사일시
    @Column(name = "TEST_DATE", columnDefinition = "datetime COMMENT '검사일시'", nullable = false)
    private LocalDate testDate;

    // 검사수량
    @Column(name = "TEST_AMOUNT", columnDefinition = "int COMMENT '검사수량'", nullable = false)
    private int testAmount;

    // 양품수량
    @Column(name = "FAIL_QUALITY_AMOUNT", columnDefinition = "int COMMENT '양품수량'", nullable = false)
    private int fairQualityAmount;

    // 부적합수량
    @Column(name = "INCONGRUITY_AMOUNT", columnDefinition = "int COMMENT '부적합수량'", nullable = false)
    private int incongruityAmount;

    // 검사결과: 합격,불합격
    @Column(name = "TEST_RESULT", columnDefinition = "bit(1) COMMENT '검사결과'")
    private boolean testResult;

    // 입고창고      삭제이유: 검사정보 페이지에서 창고 수정 불가
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "WARE_HOUSE", columnDefinition = "bigint COMMENT '입고창고'")
//    private WareHouse wareHouse;

    // 검사성적서 파일
    @Column(name = "TEST_REPORT_FILE_URL", columnDefinition = "varchar(500) COMMENT '검사성적서 파일 url'")
    private String testReportFileUrl;

    // COC 파일
    @Column(name = "COC_FILE_URL", columnDefinition = "varchar(500) COMMENT 'COC 파일 url'")
    private String cocFileUrl;

    // 검사자
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '검사자'", nullable = false)
    private User user;

    // 비고
    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;

    // 삭제여부
    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    // 검사의뢰
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "INPUT_TEST_REQUEST", columnDefinition = "bigint COMMENT '검사의뢰'", nullable = false)
    private InputTestRequest inputTestRequest;

    public void create(InputTestRequest inputTestRequest, User user) {
        setInputTestRequest(inputTestRequest);
        setUser(user);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void update(InputTestDetail newInputTestDetail, User newUser) {
        setTestDate(newInputTestDetail.testDate);
        setTestAmount(newInputTestDetail.testAmount);
        setFairQualityAmount(newInputTestDetail.fairQualityAmount);
        setIncongruityAmount(newInputTestDetail.incongruityAmount);
        setTestResult(newInputTestDetail.testResult);
        setUser(newUser);
        setNote(newInputTestDetail.note);
    }
}
