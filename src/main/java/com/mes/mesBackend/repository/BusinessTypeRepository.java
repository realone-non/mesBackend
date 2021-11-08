package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.BusinessType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessTypeRepository extends JpaCustomRepository<BusinessType, Long> {
}