package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {
    Page<BusinessType> findAllByUseYnTrue(Pageable pageable);
    BusinessType findByIdAndUseYnTrue(Long id);
}