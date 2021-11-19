package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemCheck;
import com.mes.mesBackend.repository.custom.ItemCheckRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCheckRepository extends JpaCustomRepository<ItemCheck, Long> , ItemCheckRepositoryCustom {
}