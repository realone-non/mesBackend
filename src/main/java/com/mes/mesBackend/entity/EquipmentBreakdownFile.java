package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 17-2. 설비 고장 수리내역 등록 파일
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "EQUIPMENT_BREAKDOWN_FILES")
@Data
public class EquipmentBreakdownFile extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '설비고장 수리정보 파일 고유아이디'")
    private Long id;

    @Column(name = "FILE_URL", columnDefinition = "varchar(500) COMMENT '파일 url'", nullable = false)
    private String fileUrl;

    @Column(name = "BEFORE_FILE_YN", columnDefinition = "bit(1) COMMENT '수리전 파일 여부'")
    private boolean beforeFileYn;

    @Column(name = "AFTER_FILE_YN", columnDefinition = "bit(1) COMMENT '수리후 파일 여부'")
    private boolean afterFileYn;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;  // 삭제여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT_BREAKDOWN", columnDefinition = "bigint COMMENT '설비고장수리내역'", nullable = false)
    private EquipmentBreakdown equipmentBreakdown;
}
