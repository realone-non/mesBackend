package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.TestProcess;
import com.mes.mesBackend.repository.custom.JpaCustomRepository;
import org.springframework.stereotype.Repository;

// 검사방법
@Repository
public interface TestProcessRepository extends JpaCustomRepository<TestProcess, Long> {
}