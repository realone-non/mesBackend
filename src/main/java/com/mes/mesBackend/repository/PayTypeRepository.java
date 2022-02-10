package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PayType;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayTypeRepository extends JpaCustomRepository<PayType, Long> {
}
