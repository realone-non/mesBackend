package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkCenter;
import com.mes.mesBackend.entity.WorkCenterCode;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {
    WorkCenter findByIdAndDeleteYnFalse(Long id);
    Page<WorkCenter> findAllByDeleteYnFalse(Pageable pageable);
    boolean existsAllByWorkCenterCodeAndDeleteYnFalse(WorkCenterCode workCenterCode);
}