package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkLine;
import com.mes.mesBackend.entity.WorkLineCode;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkLineRepository extends JpaRepository<WorkLine, Long> {
    WorkLine findByIdAndDeleteYnFalse(Long id);
    Page<WorkLine> findAllByDeleteYnFalse(Pageable pageable);
    boolean existsByWorkLineCodeAndDeleteYnFalse(WorkLineCode workLineCode);
}