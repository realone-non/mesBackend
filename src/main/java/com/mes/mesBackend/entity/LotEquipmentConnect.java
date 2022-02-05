package com.mes.mesBackend.entity;

import com.mes.mesBackend.entity.enumeration.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor
@NoArgsConstructor(access = PUBLIC)
@Entity(name = "LOT_EQUIPMENT_CONNECTS")
@Data
public class LotEquipmentConnect extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint COMMENT '로트 설비 연관 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_LOT", columnDefinition = "bigint COMMENT '부모 로트'", nullable = false)
    private LotMaster parentLot;    // 처음 생성된 더미로트

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CHILD_LOT", columnDefinition = "bigint COMMENT '자식 로트'", nullable = false)
    private LotMaster childLot;     // 설비에 관련해서 생성된 설비로트

    @Enumerated(STRING)
    @Column(name = "PROCESS_STATUS", columnDefinition = "varchar(255) COMMENT '프로세스 상태값'")
    private ProcessStatus processStatus;

    // 생성
    public void create(
            LotMaster parentLot,
            LotMaster childLot,
            ProcessStatus processStatus
    ) {
        setParentLot(parentLot);
        setChildLot(childLot);
        setProcessStatus(processStatus);
    }
}
