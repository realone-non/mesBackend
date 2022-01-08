package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.OutSourcingInput;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import com.mes.mesBackend.repository.custom.OutsourcingInputRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public interface OutsourcingInputRepository extends JpaCustomRepository<OutSourcingInput, Long>, OutsourcingInputRepositoryCustom {
}
