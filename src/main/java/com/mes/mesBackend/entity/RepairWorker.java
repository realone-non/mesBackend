package com.mes.mesBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

// 17-2. 설비 고장 수리 내역 수리작업자
@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "REPAIR_WORKERS")
@Data
public class RepairWorker extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '수리작업자 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "EQUIPMENT_BREAKDOWN", columnDefinition = "bigint COMMENT '설비고장수리내역'", nullable = false)
    private EquipmentBreakdown equipmentBreakdown;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER", columnDefinition = "bigint COMMENT '작업자'", nullable = false)
    private User user;

    @Column(name = "DELETE_YN", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private boolean deleteYn = false;

    public void add(EquipmentBreakdown equipmentBreakdown, User user) {
        setEquipmentBreakdown(equipmentBreakdown);
        setUser(user);
    }

    public void delete() {
        setDeleteYn(true);
    }

    public void update(User newUser) {
        setUser(newUser);
    }
}
