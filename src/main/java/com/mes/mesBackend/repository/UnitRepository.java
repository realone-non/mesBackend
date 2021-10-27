package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Page<Unit> findAllByDeleteYnFalse(Pageable pageable);
    Unit findByIdAndDeleteYnFalse(Long id);
}