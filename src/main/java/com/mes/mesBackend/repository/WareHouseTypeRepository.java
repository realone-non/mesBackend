package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WareHouseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseTypeRepository extends JpaRepository<WareHouseType, Long> {
    WareHouseType findByIdAndDeleteYnFalse(Long id);
    Page<WareHouseType> findAllByDeleteYnFalse(Pageable pageable);
}