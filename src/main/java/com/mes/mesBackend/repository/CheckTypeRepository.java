package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.CheckType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckTypeRepository extends JpaRepository<CheckType, Long> {
    CheckType findByIdAndDeleteYnFalse(Long id);
    List<CheckType> findAllByDeleteYnFalse();
}