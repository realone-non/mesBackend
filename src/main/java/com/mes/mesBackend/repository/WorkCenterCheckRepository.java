package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenterCheck;
import com.mes.mesBackend.repository.custom.WorkCenterCheckRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCenterCheckRepository extends JpaRepository<WorkCenterCheck, Long>, WorkCenterCheckRepositoryCustom {
    Page<WorkCenterCheck> findAllByDeleteYnFalse(Pageable pageable);
    WorkCenterCheck findByIdAndDeleteYnFalse(Long id);
}