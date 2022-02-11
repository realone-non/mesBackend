package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.InspectionItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "INPUT_TEST_INSPECTION_ITEMS")
@Data
public class InputTestInspectionItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '검사상세 검사항목 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "INPUT_TEST_DETAIL", columnDefinition = "bigint COMMENT '검사정보'", nullable = false)
    private InputTestDetail inputTestDetail;

    @Column(name = "INCONGRUITY_AMOUNT", columnDefinition = "int COMMENT '부적합 수량'", nullable = false)
    private int incongruityAmount;

    @Column(name = "INSPECTION_ITEM ", columnDefinition = "varchar(255) COMMENT '검사 항목'", nullable = false)
    private InspectionItem inspectionItem;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제 여부'", nullable = false)
    private boolean deleteYn;
}
