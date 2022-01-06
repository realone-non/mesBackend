package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.InputTestDetail;
import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.repository.custom.InputTestDetailRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InputTestDetailRepository extends JpaCustomRepository<InputTestDetail, Long>, InputTestDetailRepositoryCustom {
    Optional<InputTestDetail> findByInputTestRequestAndIdAndDeleteYnFalse(InputTestRequest inputTestRequest, Long id);
}
