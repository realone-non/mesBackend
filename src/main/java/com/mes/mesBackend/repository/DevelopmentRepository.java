package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Development;
import com.mes.mesBackend.repository.custom.DevelopmentRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevelopmentRepository extends JpaCustomRepository<Development, Long>, DevelopmentRepositoryCustom {
}
