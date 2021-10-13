package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Factory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {
    Factory findByIdAndDeleteYnFalse(Long id);
    Page<Factory> findAllByDeleteYnFalse(Pageable pageable);
}