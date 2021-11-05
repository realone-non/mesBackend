package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.LotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotTypeRepository extends JpaRepository<LotType, Long> {
    List<LotType> findAllByDeleteYnFalse();
    LotType findByIdAndDeleteYnFalse(Long id);
}