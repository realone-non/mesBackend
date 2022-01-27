package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.ItemAccountCode;
import com.mes.mesBackend.repository.custom.ItemAccountCodeRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAccountCodeRepository extends JpaCustomRepository<ItemAccountCode, Long> , ItemAccountCodeRepositoryCustom {
}
