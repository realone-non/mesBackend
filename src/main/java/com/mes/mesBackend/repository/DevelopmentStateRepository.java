package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.DevelopmentState;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevelopmentStateRepository extends JpaCustomRepository<DevelopmentState, Long> {
}
