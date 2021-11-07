package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.TestCriteria;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;
// 검사기준
@Repository
public interface TestCriteriaRepository extends JpaCustomRepository<TestCriteria, Long> {
}