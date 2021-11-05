package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.TestCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// 검사기준
@Repository
public interface TestCriteriaRepository extends JpaRepository<TestCriteria, Long> {
    List<TestCriteria> findAllByDeleteYnFalse();
    TestCriteria findByIdAndDeleteYnFalse(Long id);
}