package com.mes.mesBackend.repository.custom;

import com.mes.mesBackend.entity.WorkCenterCheck;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkCenterCheckRepositoryCustom {
    Page<WorkCenterCheck> findByWorkCenterAndCheckTypes(Long workCenterId, Long checkTypeId,Pageable pageable);
}
