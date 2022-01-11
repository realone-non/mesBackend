package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 수리코드
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "REPAIR_CODES")
@Data
public class RepairCode extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수리코드 고유아이디'")
    private Long id;

    @Column(name = "REPAIR_CODE", columnDefinition = "varchar(255) COMMENT '수리코드'", nullable = false)
    private String repairCode;

    @Column(name = "REPAIR_CONTENT", columnDefinition = "varchar(500) COMMENT '수리내용'", nullable = false)
    private String repairContent;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void delete() {
        setDeleteYn(true);
    }

    public void update(RepairCode newRepairCode) {
        setRepairCode(newRepairCode.getRepairCode());
        setRepairContent(newRepairCode.getRepairContent());
    }
}
