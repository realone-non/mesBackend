package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkProcess;
import com.mes.mesBackend.entity.WorkProcessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProcessRepository extends JpaRepository<WorkProcess, Long> {
    Page<WorkProcess> findAllByDeleteYnFalse(Pageable pageable);
    WorkProcess findByIdAndDeleteYnFalse(Long id);
    boolean existsByWorkProcessCodeAndDeleteYnFalse(WorkProcessCode workProcessCode);
}