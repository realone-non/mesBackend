package com.mes.mesBackend.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * 16-3. 부적합 등록 1
 * 검색: 공장,검사유형,등록기간,생산품목그룹,품목,적용업체
 * 등록일시
 * 검사유형
 * 품번
 * 품명
 * 검사번호
 * 부적합유형
 * 부적합처리방법
 * 부적합적용업체
 * 부적합수량
 * 기타비용 내역
 * 처리 부서명
 * 처리 담당자
 * 부적합 원인
 * 부적합 조치내용
 * 유효성 검증 결과
 * 개선방안
 * 발생이미지파일
 * 개선후 이미지 파일
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "INCONGRUITIES")
@Data
public class Incongruity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '부적합정보 고유아이디'")
    private Long id;            // 부적합 등록 고유아이디

    @Column(name = "REGISTRATION_DATE", columnDefinition = "date COMMENT '검사일시'")
    private LocalDate registrationDate;             // 검사일시

    @Column(name = "TEST_CATEGORY", columnDefinition = "bigint COMMENT '검사유형'")
    private TestCategory testCategory;              // 검사유형

    @Column(name = "ITEM", columnDefinition = "bigint COMMENT '품목'")
    private Item item;          // 품목

    @Column(name = "TEST_NO", columnDefinition = "varchar(255) COMMENT '검사번호'")
    private String testNo;             // 검사번호

    @Column(name = "INCONGRUITY_TYPE", columnDefinition = "varchar(255) COMMENT '부적합유형'")
    private String incongruityType;     // 부적합유형

    @Column(name = "INCONGRUITY_TREATMENT_METHOD", columnDefinition = "varchar(255) COMMENT '부적합 처리방법'")
    private String incongruityTreatmentMethod;     // 부적합 처리방법

    @Column(name = "APPLICANT", columnDefinition = "varchar(255) COMMENT '부적합 적용업체'")
    private String applicant;           // 부적합 적용업체

    @Column(name = "INCONGRUITY_AMOUNT", columnDefinition = "int COMMENT '부적합수량'")
    private int incongruityAmount;       // 부적합 수량

    @Column(name = "OTHER_COST_CONTENT", columnDefinition = "varchar(255) COMMENT '기타비용내역'")
    private String otherCostContent;        // 기타비용내역

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "DEPARTMENT", columnDefinition = "bigint COMMENT '부서'")
    private Department department;          // 처리부서명

    @Column(name = "MANAGER", columnDefinition = "varchar(255) COMMENT '처리담당자'")
    private String manager;                 // 처리담당자

    @Column(name = "INCONGRUITY_CAUSE", columnDefinition = "varchar(255) COMMENT '부적합원인'")
    private String incongruityCause;        // 부적합원인

    @Column(name = "INCONGRUITY_ACTION", columnDefinition = "varchar(255) COMMENT '부적합 조치내용'")
    private String incongruityAction;       // 부적합 조치내용

    @Column(name = "VAILD_TEST", columnDefinition = "varchar(255) COMMENT '유효성 검증 결과'")
    private String vaildTest;               // 유효성 검증 결과

    @Column(name = "UPGRADE_PLAN", columnDefinition = "varchar(255) COMMENT '개선방안'")
    private String upgradePlan;             // 개선방안

    @Column(name = "OCCURRENCE_FILE_URL", columnDefinition = "varchar(255) COMMENT '발생이미지 파일'")
    private String occurrenceFileUrl;       // 발생이미지 파일

    @Column(name = "IMPROVEMENT_FILE_URL", columnDefinition = "varchar(255) COMMENT '개선 이미지 파일'")
    private String improvementFileUrl;      // 개선 이미지 파일
}
