package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.InputTestRequest;
import com.mes.mesBackend.repository.custom.InputTestRequestRepositoryCustom;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

// 14-1. 검사의뢰 등록
@Repository
public interface InputTestRequestRepository extends JpaCustomRepository<InputTestRequest, Long>, InputTestRequestRepositoryCustom {
}
