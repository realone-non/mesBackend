package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.entity.WorkCenterCheckDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkCenterCheckDetailRepository extends JpaRepository<WorkCenterCheckDetail, Long> {
    List<WorkCenterCheckDetail> findAllByWorkCenterCheckAndDeleteYnFalse(WorkCenterCheck workCenterCheck);
    WorkCenterCheckDetail findByIdAndDeleteYnFalse(Long id);
    WorkCenterCheckDetail findByIdAndWorkCenterCheckAndDeleteYnFalse(Long id, WorkCenterCheck workCenterCheck);
}