package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ProduceOrder;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.ProduceOrderRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduceOrderRepository extends JpaCustomRepository<ProduceOrder, Long>, ProduceOrderRepositoryCustom {
}
