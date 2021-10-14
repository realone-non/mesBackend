package com.mes.mesBackend.repository;

import com.mes.mesBackend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByIdAndDeleteYnFalse(Long id);
    Page<Employee> findAllByDeleteYnFalse(Pageable pageable);
}