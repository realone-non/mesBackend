package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.WareHouse;
import com.mes.mesBackend.entity.WareHouseType;
import com.mes.mesBackend.entity.WareHouseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouse, Long> {
    WareHouse findByIdAndDeleteYnFalse(Long id);
    Page<WareHouse> findAllByDeleteYnFalse(Pageable pageable);
}