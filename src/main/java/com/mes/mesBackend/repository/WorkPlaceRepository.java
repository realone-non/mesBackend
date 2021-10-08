package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WorkPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 사업장 repository
@Repository
public interface WorkPlaceRepository extends JpaRepository<WorkPlace, Long> {
    WorkPlace findByIdAndDeleteYnFalse(Long id);
    Page<WorkPlace> findAllByDeleteYnFalse(Pageable pageable);
}