package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.TestProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 검사방법
@Repository
public interface TestProcessRepository extends JpaRepository<TestProcess, Long> {
    List<TestProcess> findAllByDeleteYnFalse();
    TestProcess findByIdAndDeleteYnFalse(Long id);
}