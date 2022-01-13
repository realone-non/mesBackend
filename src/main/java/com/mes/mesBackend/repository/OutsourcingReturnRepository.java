package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.OutsourcingReturn;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.OutsourcingReturnRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface OutsourcingReturnRepository extends JpaCustomRepository<OutsourcingReturn, Long>, OutsourcingReturnRepositoryCustom {
}
