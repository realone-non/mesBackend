package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BusinessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {
}