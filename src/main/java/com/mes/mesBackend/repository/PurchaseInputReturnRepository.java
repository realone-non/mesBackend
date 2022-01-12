package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.PurchaseInputReturn;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.PurchaseInputReturnRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseInputReturnRepository extends JpaCustomRepository<PurchaseInputReturn, Long>, PurchaseInputReturnRepositoryCustom {
}
