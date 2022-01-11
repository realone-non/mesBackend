package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.RepairItem;
import com.mes.mesBackend.entity.RepairPart;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 수리부품정보
@Repository
public interface RepairPartRepository extends JpaCustomRepository<RepairPart, Long> {
    Optional<RepairPart> findByIdAndRepairItemAndDeleteYnFalse(Long id, RepairItem repairItem);
    List<RepairPart> findAllByRepairItemAndDeleteYnFalse(RepairItem repairItem);
}
