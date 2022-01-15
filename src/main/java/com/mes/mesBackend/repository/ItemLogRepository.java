package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemLog;
import com.mes.mesBackend.repository.custom.ItemLogRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLogRepository extends JpaCustomRepository<ItemLog, Long>, ItemLogRepositoryCustom {
}
