package com.mes.mesBackend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/*
 * BOM등록 -> BOM 마스터 정보
 * 검색: 품목계정, 품목그룹, 품목, 하위품목
 * 품번 (AA01-AF2-E001DB)
 * 품명 (EMI FILTER [AF2-E001DB])
 * 품목계정 (제품)
 * 제조사품번 (EP2-A003D, null)
 * BOM 번호 (1,2)
 * 유효시작일 (2020.11.5)
 * 유효종료일 (2999.12.31)
 * 개발상태 (개발완료)
 * 비고
 * 승일일시
 * 사용
 * */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "BOM_MASTERS")
@Data
public class BomMaster extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT 'BOM등록 고유아이디'")
    private Long id;

    // 다대일 단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_NO", columnDefinition = "bigint COMMENT '품목'")
    private Item itemNo;            // 품번,품명,품목계정,제조사품번,개발상태

    @Column(name = "BOM_NO", columnDefinition = "bigint COMMENT 'BOM번호'")
    private Long bomNo;             // BOM번호

    @Column(name = "START_DATE", columnDefinition = "date COMMENT '유효시작일'")
    private LocalDate startDate;    // 유효시작일

    @Column(name = "END_DATE", columnDefinition = "date COMMENT '유효종료일'")
    private LocalDate endDate;      // 유효종료일

    @Enumerated(EnumType.STRING)
    @Column(name = "DEVELOP_STATUS", nullable = false, columnDefinition = "varchar(255) COMMENT '개발상태'")
    private DevelopStatus developStatus = DevelopStatus.BEFORE;  // 개발상태

    @Column(name = "NOTE", columnDefinition = "varchar(255) COMMENT '비고'")
    private String note;            // 비고

    @Column(name = "APPROVALDATE", columnDefinition = "date COMMENT '승인일시'")
    private LocalDate approvalDate;     // 승인일시

    @Column(name = "USE_YN", nullable = false, columnDefinition = "bit(1) COMMENT '사용여부'")
    private Boolean useYn = true;      //  사용여부

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'")
    private boolean deleteYn = false;  // 삭제여부
}
