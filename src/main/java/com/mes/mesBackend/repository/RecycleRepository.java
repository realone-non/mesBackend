package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Recycle;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.RecycleRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface RecycleRepository extends JpaCustomRepository<Recycle, Long>, RecycleRepositoryCustom {
}
