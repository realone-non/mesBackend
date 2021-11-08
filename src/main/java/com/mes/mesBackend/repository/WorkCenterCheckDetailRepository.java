package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.entity.WorkCenterCheckDetail;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkCenterCheckDetailRepository extends JpaCustomRepository<WorkCenterCheckDetail, Long> {
    List<WorkCenterCheckDetail> findAllByWorkCenterCheckAndDeleteYnFalse(WorkCenterCheck workCenterCheck);
    WorkCenterCheckDetail findByIdAndWorkCenterCheckAndDeleteYnFalse(Long id, WorkCenterCheck workCenterCheck);
}